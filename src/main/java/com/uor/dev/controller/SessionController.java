package com.uor.dev.controller;

import com.uor.dev.payload.session.CreateSessionRequestDTO;
import com.uor.dev.payload.session.SessionResponseDTO;
import com.uor.dev.payload.session.UpdateSessionRequestDTO;
import com.uor.dev.service.SessionService;
import com.uor.dev.util.ResponseEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

@Path("/api/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionController {

  @Inject
  SessionService sessionService;

  @GET
  @Path("/all")
  public ResponseEntity<List<SessionResponseDTO>> getAllSessions() {
    List<SessionResponseDTO> sessions = sessionService.getAllSessions();
    return ResponseEntity.ok(sessions);
  }

  @GET
  @Path("/{id}")
  public ResponseEntity<SessionResponseDTO> getSessionById(@PathParam("id") int id) {
    Optional<SessionResponseDTO> session = sessionService.getSessionById(id);
    return session.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Session not found"));
  }

  @DELETE
  @Path("/delete/{id}")
  public ResponseEntity<Void> deleteSession(@PathParam("id") int id) {
    boolean deleted = sessionService.deleteSession(id);
    if (deleted) {
      return ResponseEntity.noContent();
    } else {
      return ResponseEntity.notFound("Session not found");
    }
  }

  @POST
  @Path("/add/course/{courseId}/lecturer/{lecturerId}")
  public ResponseEntity<SessionResponseDTO> addSession(@PathParam("courseId") int courseId,
                                                       @PathParam("lecturerId") int lecturerId,
                                                       CreateSessionRequestDTO createSessionRequestDTO
  ) {
    SessionResponseDTO createdSession = sessionService.addSession(courseId, lecturerId, createSessionRequestDTO);
    return ResponseEntity.created(createdSession);
  }

  @PUT
  @Path("/update/{id}")
  public ResponseEntity<SessionResponseDTO> updateSession(@PathParam("id") int id, UpdateSessionRequestDTO session) {
    Optional<SessionResponseDTO> updatedSession = sessionService.updateSession(id, session);
    return updatedSession.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Session not found"));
  }

  @GET
  @Path("/course/{courseId}")
  public ResponseEntity<List<SessionResponseDTO>> getSessionsByCourseId(@PathParam("courseId") int courseId) {
    List<SessionResponseDTO> sessions = sessionService.getSessionsByCourseId(courseId);
    return ResponseEntity.ok(sessions);
  }
}
