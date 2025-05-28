package com.uor.dev.controller;

import com.uor.dev.payload.enrollment.CreateEnrollmentRequestDTO;
import com.uor.dev.payload.enrollment.EnrollmentResponseDTO;
import com.uor.dev.service.EnrollmentService;
import com.uor.dev.util.ResponseEntity;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

@Path("/api/enrollments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnrollmentController {

  @Inject
  EnrollmentService enrollmentService;

  @GET
  @Path("/all")
  public ResponseEntity<List<EnrollmentResponseDTO>> getAllEnrollments() {
    List<EnrollmentResponseDTO> enrollments = enrollmentService.getAllEnrollments();
    return ResponseEntity.ok(enrollments);
  }

  @GET
  @Path("/{id}")
  public ResponseEntity<EnrollmentResponseDTO> getEnrollmentById(@PathParam("id") int id) {
    Optional<EnrollmentResponseDTO> enrollment = enrollmentService.getEnrollmentById(id);
    return enrollment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Enrollment not found"));
  }

  @DELETE
  @Path("/delete/{id}")
  public ResponseEntity<Void> deleteEnrollment(@PathParam("id") int id) {
    boolean deleted = enrollmentService.deleteEnrollment(id);
    if (deleted) {
      return ResponseEntity.noContent();
    } else {
      return ResponseEntity.notFound("Enrollment not found");
    }
  }

  @GET
  @Path("/student/{studentId}")
  public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentsByStudentId(@PathParam("studentId") int studentId) {
    List<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
    return ResponseEntity.ok(enrollments);
  }

  @POST
  @Path("/{courseID}")
  public ResponseEntity<EnrollmentResponseDTO> addEnrollment(
      @PathParam("courseID") int courseId,
      @Valid CreateEnrollmentRequestDTO enrollmentRequest
  ) {
    EnrollmentResponseDTO createdEnrollment = enrollmentService.addEnrollment(enrollmentRequest, courseId);
    return ResponseEntity.created(createdEnrollment);
  }

  @GET
  @Path("/course/{courseId}")
  public ResponseEntity<List<EnrollmentResponseDTO>> getEnrollmentsByCourseId(@PathParam("courseId") int courseId) {
    List<EnrollmentResponseDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
    return ResponseEntity.ok(enrollments);
  }
}
