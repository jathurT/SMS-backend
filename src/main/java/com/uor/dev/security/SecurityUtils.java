package com.uor.dev.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

@RequestScoped
public class SecurityUtils {

  @Context
  SecurityContext securityContext;

  // Get the current authenticated user
  public UserPrincipal getCurrentUser() {
    // In JAX-RS, we can get the current user from the security context
    // This will be set by our authentication filter
    return (UserPrincipal) securityContext.getUserPrincipal();
  }

  // Check if current user has a specific role
  public boolean hasRole(String role) {
    UserPrincipal user = getCurrentUser();
    return user != null && user.hasRole(role);
  }

  // Check if current user has any of the specified roles
  public boolean hasAnyRole(String... roles) {
    UserPrincipal user = getCurrentUser();
    return user != null && user.hasAnyRole(roles);
  }

  // Check if current user is an admin
  public boolean isAdmin() {
    return hasAnyRole(Roles.ADMIN);
  }

  // Get current user's ID - useful for data filtering
  public String getCurrentUserId() {
    UserPrincipal user = getCurrentUser();
    return user != null ? user.getUserId() : null;
  }
}
