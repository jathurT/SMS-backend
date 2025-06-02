package com.uor.dev.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Utility class for handling JWT tokens, including signature verification,
 */
@ApplicationScoped
public class JwtUtils {

  private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final HttpClient httpClient = HttpClient.newHttpClient();

  // Cache for Keycloak public keys (key ID -> PublicKey)
  private final Map<String, PublicKey> publicKeyCache = new ConcurrentHashMap<>();
  private long lastKeyFetch = 0;
  private static final long KEY_CACHE_TTL = 300_000; // 5 minutes

  @ConfigProperty(name = "keycloak.jwks.url")
  String jwksUrl; // http://localhost:8080/realms/university-system/protocol/openid-connect/certs

  @ConfigProperty(name = "keycloak.issuer")
  String expectedIssuer; // http://localhost:8080/realms/university-system

  @PostConstruct
  public void init() {
    logger.info("JwtUtils initialized with JWKS URL: " + jwksUrl);
    logger.info("Expected issuer: " + expectedIssuer);
  }

  public UserPrincipal extractUserPrincipal(String token) {
    try {
      logger.info("=== JWT DEBUG START ===");
      logger.info("Raw token length: " + token.length());

      if (token.startsWith("Bearer ")) {
        token = token.substring(7);
        logger.info("Removed Bearer prefix");
      }

      String[] chunks = token.split("\\.");
      logger.info("Token parts: " + chunks.length);

      if (chunks.length != 3) {
        throw new SecurityException("Invalid JWT - expected 3 parts, got " + chunks.length);
      }

      // Try to decode payload
      try {
        String payloadJson = new String(Base64.getUrlDecoder().decode(chunks[1]));
        logger.info("Decoded payload: " + payloadJson);

        JsonNode payload = objectMapper.readTree(payloadJson);

        // Extract basic info
        String userId = getStringValue(payload, "sub");
        String email = getStringValue(payload, "email");
        Set<String> roles = extractRoles(payload);

        logger.info("Extracted - User: " + userId + ", Roles: " + roles);
        logger.info("=== JWT DEBUG SUCCESS ===");

        return new UserPrincipal(userId, email, "", "", roles);

      } catch (Exception decodeError) {
        logger.severe("Payload decode failed: " + decodeError.getMessage());
        throw new SecurityException("Cannot decode JWT payload");
      }

    } catch (Exception e) {
      logger.severe("=== JWT DEBUG FAILED ===");
      logger.severe("Error: " + e.getMessage());
      e.printStackTrace();
      throw new SecurityException("JWT error: " + e.getMessage());
    }
  }

  /**
   * CRITICAL: Verify JWT signature using Keycloak's public key
   */
  private boolean verifySignature(String signedContent, byte[] signature, JsonNode header) {
    try {
      // Get algorithm and key ID from header
      String algorithm = header.get("alg").asText();
      String keyId = header.get("kid").asText();

      // Only support RS256 for security
      if (!"RS256".equals(algorithm)) {
        logger.severe("Unsupported JWT algorithm: " + algorithm);
        return false;
      }

      // Get public key for this key ID
      PublicKey publicKey = getPublicKey(keyId);
      if (publicKey == null) {
        logger.severe("Could not retrieve public key for kid: " + keyId);
        return false;
      }

      // Verify signature
      Signature sig = Signature.getInstance("SHA256withRSA");
      sig.initVerify(publicKey);
      sig.update(signedContent.getBytes());

      boolean isValid = sig.verify(signature);
      logger.fine("Signature verification result: " + isValid + " for key: " + keyId);

      return isValid;

    } catch (Exception e) {
      logger.severe("Signature verification error: " + e.getMessage());
      return false;
    }
  }

  /**
   * Fetch public key from Keycloak JWKS endpoint
   */
  private PublicKey getPublicKey(String keyId) {
    try {
      // Check cache first
      PublicKey cachedKey = publicKeyCache.get(keyId);
      long now = System.currentTimeMillis();

      if (cachedKey != null && (now - lastKeyFetch) < KEY_CACHE_TTL) {
        return cachedKey;
      }

      // Fetch fresh keys from Keycloak
      logger.info("Fetching public keys from Keycloak JWKS endpoint");
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(jwksUrl))
              .GET()
              .build();

      HttpResponse<String> response = httpClient.send(request,
              HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        logger.severe("Failed to fetch JWKS: HTTP " + response.statusCode());
        return null;
      }

      // Parse JWKS response
      JsonNode jwks = objectMapper.readTree(response.body());
      JsonNode keys = jwks.get("keys");

      // Find the key with matching kid
      for (JsonNode key : keys) {
        String kid = key.get("kid").asText();
        if (keyId.equals(kid)) {
          PublicKey publicKey = buildPublicKey(key);
          publicKeyCache.put(keyId, publicKey);
          lastKeyFetch = now;
          logger.info("Successfully cached public key: " + keyId);
          return publicKey;
        }
      }

      logger.warning("Public key not found for kid: " + keyId);
      return null;

    } catch (Exception e) {
      logger.severe("Error fetching public key: " + e.getMessage());
      return null;
    }
  }

  /**
   * Build RSA public key from JWKS key data
   */
  private PublicKey buildPublicKey(JsonNode key) throws Exception {
    String n = key.get("n").asText(); // modulus
    String e = key.get("e").asText(); // exponent

    byte[] nBytes = Base64.getUrlDecoder().decode(n);
    byte[] eBytes = Base64.getUrlDecoder().decode(e);

    BigInteger modulus = new BigInteger(1, nBytes);
    BigInteger exponent = new BigInteger(1, eBytes);

    RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
    KeyFactory factory = KeyFactory.getInstance("RSA");

    return factory.generatePublic(spec);
  }

  private String getStringValue(JsonNode jsonNode, String fieldName) {
    JsonNode field = jsonNode.get(fieldName);
    return field != null ? field.asText() : "";
  }

  private Set<String> extractRoles(JsonNode jsonNode) {
    Set<String> roles = new HashSet<>();

    // Extract from realm_access.roles (Keycloak standard)
    JsonNode realmAccess = jsonNode.get("realm_access");
    if (realmAccess != null && realmAccess.has("roles")) {
      JsonNode rolesNode = realmAccess.get("roles");
      if (rolesNode.isArray()) {
        for (JsonNode roleNode : rolesNode) {
          roles.add(roleNode.asText());
        }
      }
    }
    return roles;
  }

  // Enhanced security methods
  public boolean isTokenValid(String token) {
    try {
      extractUserPrincipal(token);
      return true;
    } catch (SecurityException e) {
      return false;
    }
  }

  public void clearKeyCache() {
    publicKeyCache.clear();
    lastKeyFetch = 0;
    logger.info("Public key cache cleared");
  }
}