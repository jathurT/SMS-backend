package com.uor.dev.controller;

import com.uor.dev.payload.lecturer.CreateLecturerRequestDTO;
import com.uor.dev.payload.lecturer.LectureCourseResponseDTO;
import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import com.uor.dev.payload.lecturer.UpdateLecturerRequestDTO;
import com.uor.dev.service.LectureService;
import com.uor.dev.util.ResponseEntity;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

@Path("/api/lecturers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LecturerController {

  @Inject
  LectureService lectureService;

  @GET
  @Path("/all")
  public ResponseEntity<List<LecturerResponseDTO>> getAllLecturers() {
    List<LecturerResponseDTO> lecturers = lectureService.getAllLecturers();
    return ResponseEntity.ok(lecturers);
  }

  @GET
  @Path("/{id}")
  public ResponseEntity<LecturerResponseDTO> getLecturerById(@PathParam("id") int id) {
    Optional<LecturerResponseDTO> lecturer = lectureService.getLecturerById(id);
    return lecturer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Lecturer not found"));
  }

  @POST
  @Path("/add")
  public ResponseEntity<LecturerResponseDTO> addLecturer(@Valid CreateLecturerRequestDTO lecturer) {
    LecturerResponseDTO createdLecturer = lectureService.addLecturer(lecturer);
    return ResponseEntity.created(createdLecturer);
  }

  @PUT
  @Path("/update/{id}")
  public ResponseEntity<LecturerResponseDTO> updateLecturer(@PathParam("id") int id, @Valid UpdateLecturerRequestDTO lecturer) {
    Optional<LecturerResponseDTO> updatedLecturer = lectureService.updateLecturer(id, lecturer);
    return updatedLecturer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Lecturer not found"));
  }

  @DELETE
  @Path("/delete/{id}")
  public ResponseEntity<Void> deleteLecturer(@PathParam("id") int id) {
    boolean deleted = lectureService.deleteLecturer(id);
    if (deleted) {
      return ResponseEntity.noContent();
    } else {
      return ResponseEntity.notFound("Lecturer not found");
    }
  }

  @GET
  @Path("/details/{id}")
  public ResponseEntity<LectureCourseResponseDTO> getLecturerDetails(@PathParam("id") int id) {
    Optional<LectureCourseResponseDTO> lecturerDetails = lectureService.getLecturerDetails(id);
    return lecturerDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Lecturer details not found"));
  }

}
