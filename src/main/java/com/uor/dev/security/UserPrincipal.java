package com.uor.dev.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UserPrincipal {
  // Getters for accessing user information
  private final String userId;
  private final String email;
  private final String firstName;
  private final String lastName;
  private final Set<String> roles;

  // Convenience methods for role checking - makes our code more readable
  public boolean hasRole(String role) {
    return roles.contains(role);
  }

  public boolean hasAnyRole(String... roles) {
    for (String role : roles) {
      if (this.roles.contains(role)) {
        return true;
      }
    }
    return false;
  }

  // Check if user is any type of admin
  public boolean isAdmin() {
    return hasAnyRole(Roles.ADMIN);
  }

  @Override
  public String toString() {
    return String.format("UserPrincipal{userId='%s', email='%s', firstName='%s', lastName='%s', roles=%s}",
            userId, email, firstName, lastName, roles);
  }
}