package com.uor.dev.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.logging.Logger;

@Provider
@RequireRole({}) // This annotation makes this filter apply to @RequireRole annotated methods
public class AuthorizationFilter implements ContainerRequestFilter {

  private static final Logger logger = Logger.getLogger(AuthorizationFilter.class.getName());

  @Context
  ResourceInfo resourceInfo; // Gives us access to the method being called

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    UserPrincipal principal = (UserPrincipal) requestContext.getProperty("userPrincipal");

    if (principal == null) {
      logger.warning("No user principal found - authentication may have failed");
      requestContext.abortWith(
              Response.status(Response.Status.UNAUTHORIZED)
                      .entity("{\"error\": \"Authentication required\"}")
                      .type("application/json")
                      .build()
      );
      return;
    }

    // Get required roles from the method annotation
    String[] requiredRoles = getRequiredRoles();

    if (requiredRoles.length > 0) {
      boolean hasRequiredRole = principal.hasAnyRole(requiredRoles);

      if (!hasRequiredRole) {
        logger.warning("User " + principal.getUserId() + " lacks required roles: " +
                String.join(", ", requiredRoles) + ". User has: " + principal.getRoles());

        requestContext.abortWith(
                Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\": \"Insufficient permissions\"}")
                        .type("application/json")
                        .build()
        );
        return;
      }

      logger.info("Authorization successful for user " + principal.getUserId() +
              " with roles: " + principal.getRoles());
    }
  }

  // Extract required roles from method or class annotation
  private String[] getRequiredRoles() {
    RequireRole methodAnnotation = resourceInfo.getResourceMethod().getAnnotation(RequireRole.class);
    if (methodAnnotation != null) {
      return methodAnnotation.value();
    }

    RequireRole classAnnotation = resourceInfo.getResourceClass().getAnnotation(RequireRole.class);
    if (classAnnotation != null) {
      return classAnnotation.value();
    }

    return new String[0];
  }
}