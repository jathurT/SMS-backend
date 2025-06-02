package com.uor.dev.security;

public final class Roles {
  public static final String ADMIN = "ADMIN";
  public static final String DEPARTMENT_ADMIN = "DEPARTMENT_ADMIN";
  public static final String LECTURER = "LECTURER";
  public static final String STUDENT = "STUDENT";

  private Roles() {
    // Private constructor prevents instantiation - this is a utility class
  }
}