package com.uor.dev.security;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.logging.Logger;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

  private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());

  @Inject
  JwtUtils jwtUtils;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    String path = requestContext.getUriInfo().getPath();
    String method = requestContext.getMethod();

    logger.info("Processing request: " + method + " " + path);

    // Skip authentication for certain paths
    if (isPublicPath(path) || isOptionsRequest(method)) {
      logger.info("Skipping authentication for path: " + path + " method: " + method);
      return;
    }

    // Extract Authorization header
    String authHeader = requestContext.getHeaderString("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      logger.warning("Missing or invalid Authorization header for path: " + path);
      logger.warning("Authorization header: " + authHeader);
      requestContext.abortWith(
              Response.status(Response.Status.UNAUTHORIZED)
                      .entity("{\"error\": \"Missing or invalid Authorization header\"}")
                      .type("application/json")
                      .build()
      );
      return;
    }

    String token = authHeader.substring(7);

    try {
      // Extract user principal and set in request context
      UserPrincipal principal = jwtUtils.extractUserPrincipal(token);
      requestContext.setProperty("userPrincipal", principal);

      logger.info("Authentication successful for user: " + principal.getUserId());

    } catch (Exception e) {
      logger.severe("Token validation failed: " + e.getMessage());
      requestContext.abortWith(
              Response.status(Response.Status.UNAUTHORIZED)
                      .entity("{\"error\": \"Invalid token: " + e.getMessage() + "\"}")
                      .type("application/json")
                      .build()
      );
    }
  }

  // Define which paths don't require authentication
  private boolean isPublicPath(String path) {
    return path.startsWith("health") ||
            path.startsWith("metrics") ||
            path.startsWith("openapi") ||
            path.startsWith("swagger-ui") ||
            // Add any public endpoints here if needed
            path.equals("auth/login") ||
            path.equals("auth/register");
  }

  // Allow OPTIONS requests for CORS preflight
  private boolean isOptionsRequest(String method) {
    return "OPTIONS".equalsIgnoreCase(method);
  }
}