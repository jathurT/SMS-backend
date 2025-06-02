package com.uor.dev.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception e) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setPath(e.getClass().getSimpleName());

    switch (e) {
      case EntityNotFoundException ignored -> {
        errorResponse.setStatus(404);
        errorResponse.setMessage("Entity not found");
        errorResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .type("application/json")
                .build();
      }
      case NotFoundException ignored -> {
        errorResponse.setStatus(404);
        errorResponse.setMessage("Resource not found");
        errorResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .type("application/json")
                .build();
      }
      case IllegalArgumentException ignored -> {
        errorResponse.setStatus(400);
        errorResponse.setMessage("Bad Request");
        errorResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type("application/json")
                .build();
      }
      case ConstraintViolationException ignored -> {
        errorResponse.setStatus(400);
        errorResponse.setMessage("Validation Error");
        errorResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .type("application/json")
                .build();
      }

      case NotAuthorizedException ignored -> {
        errorResponse.setStatus(401);
        errorResponse.setMessage("Unauthorized");
        errorResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorResponse)
                .type("application/json")
                .build();
      }
      default -> {
        errorResponse.setStatus(500);
        errorResponse.setMessage("Internal Server Error");
        errorResponse.setMessage(e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .type("application/json")
                .build();
      }
    }
  }
}

