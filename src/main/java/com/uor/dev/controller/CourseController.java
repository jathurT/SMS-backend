package com.uor.dev.controller;

import com.uor.dev.payload.course.CourseAnalyticResponseDTO;
import com.uor.dev.payload.course.CourseResponseDTO;
import com.uor.dev.payload.course.CreateCourseRequestDTO;
import com.uor.dev.service.CourseService;
import com.uor.dev.util.ResponseEntity;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

@Path("/api/courses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CourseController {

  @Inject
  CourseService courseService;

  @GET
  @Path("/all")
  public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
    List<CourseResponseDTO> courses = courseService.getAllCourses();
    return ResponseEntity.ok(courses);
  }

  @GET
  @Path("/{id}")
  public ResponseEntity<CourseResponseDTO> getCourseById(@PathParam("id") int id) {
    Optional<CourseResponseDTO> course = courseService.getCourseById(id);
    return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Course not found"));
  }

  @DELETE
  @Path("/delete/{id}")
  public ResponseEntity<Void> deleteCourse(@PathParam("id") int id) {
    boolean deleted = courseService.deleteCourse(id);
    if (deleted) {
      return ResponseEntity.noContent();
    } else {
      return ResponseEntity.notFound("Course not found");
    }
  }

  @POST
  @Path("/add")
  public ResponseEntity<CourseResponseDTO> addCourse(@Valid CreateCourseRequestDTO course) {
    CourseResponseDTO createdCourse = courseService.addCourse(course);
    return ResponseEntity.created(createdCourse);
  }

  @PUT
  @Path("/update/{id}")
  public ResponseEntity<CourseResponseDTO> updateCourse(@PathParam("id") int id, @Valid CreateCourseRequestDTO course) {
    Optional<CourseResponseDTO> updatedCourse = courseService.updateCourse(id, course);
    return updatedCourse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Course not found"));
  }

  @POST
  @Path("/add-lecturer/{courseId}/{lecturerId}")
  public ResponseEntity<CourseResponseDTO> addLecturerToCourse(@PathParam("courseId") int courseId, @PathParam("lecturerId") int lecturerId) {
    Optional<CourseResponseDTO> updatedCourse = courseService.addLecturerToCourse(courseId, lecturerId);
    return updatedCourse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Course or Lecturer not found"));
  }

  @GET
  @Path("/details/{id}")
  public ResponseEntity<CourseAnalyticResponseDTO> getCourseDetails(@PathParam("id") int id) {
    Optional<CourseAnalyticResponseDTO> course = courseService.getCourseDetailsById(id);
    return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Course not found"));
  }

  @GET
  @Path("/analytics")
  public ResponseEntity<List<CourseAnalyticResponseDTO>> getCourseAnalytics() {
    List<CourseAnalyticResponseDTO> analytics = courseService.getCourseAnalytics();
    return ResponseEntity.ok(analytics);
  }
}
