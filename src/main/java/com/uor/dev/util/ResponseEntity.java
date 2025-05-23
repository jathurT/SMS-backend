package com.uor.dev.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseEntity<T> {

  private final T body;
  private final int statusCode;

  public static <T> ResponseEntity<T> ok(T entity) {
    return new ResponseEntity<>(entity, 200);
  }

  public static <T> ResponseEntity<T> created(T entity) {
    return new ResponseEntity<>(entity, 201);
  }

  public static <T> ResponseEntity<T> notFound(String studentNotFound) {
    return new ResponseEntity<>(null, 404);
  }

  public static <T> ResponseEntity<T> badRequest() {
    return new ResponseEntity<>(null, 400);
  }

  public static <T> ResponseEntity<T> internalServerError() {
    return new ResponseEntity<>(null, 500);
  }

  public static <T> ResponseEntity<T> unauthorized() {
    return new ResponseEntity<>(null, 401);
  }

  public static <T> ResponseEntity<T> forbidden() {
    return new ResponseEntity<>(null, 403);
  }

  public static <T> ResponseEntity<T> noContent() {
    return new ResponseEntity<>(null, 204);
  }

  public static <T> ResponseEntity<T> accepted() {
    return new ResponseEntity<>(null, 202);
  }

  public static <T> ResponseEntity<T> notModified() {
    return new ResponseEntity<>(null, 304);
  }
}
