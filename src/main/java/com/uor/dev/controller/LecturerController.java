package com.uor.dev.controller;

import com.uor.dev.entity.Lecturer;
import com.uor.dev.payload.lecturer.CreateLecturerRequestDTO;
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
  public ResponseEntity<List<Lecturer>> getAllLecturers() {
    List<Lecturer> lecturers = lectureService.getAllLecturers();
    return ResponseEntity.ok(lecturers);
  }

  @GET
  @Path("/{id}")
  public ResponseEntity<Lecturer> getLecturerById(@PathParam("id") int id) {
    Optional<Lecturer> lecturer = lectureService.getLecturerById(id);
    return lecturer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Lecturer not found"));
  }

  @POST
  @Path("/add")
  public ResponseEntity<Lecturer> addLecturer(@Valid CreateLecturerRequestDTO lecturer) {
    Lecturer createdLecturer = lectureService.addLecturer(lecturer);
    return ResponseEntity.created(createdLecturer);
  }

  @PUT
  @Path("/update/{id}")
  public ResponseEntity<Lecturer> updateLecturer(@PathParam("id") int id, @Valid UpdateLecturerRequestDTO lecturer) {
    Optional<Lecturer> updatedLecturer = lectureService.updateLecturer(id, lecturer);
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

}
