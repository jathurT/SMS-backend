package com.uor.dev.controller;

import com.uor.dev.entity.Course;
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
  public ResponseEntity<List<Course>> getAllCourses() {
    List<Course> courses = courseService.getAllCourses();
    return ResponseEntity.ok(courses);
  }

  @GET
  @Path("/{id}")
  public ResponseEntity<Course> getCourseById(@PathParam("id") int id) {
    Optional<Course> course = courseService.getCourseById(id);
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
  public ResponseEntity<Course> addCourse(@Valid CreateCourseRequestDTO course) {
    Course createdCourse = courseService.addCourse(course);
    return ResponseEntity.created(createdCourse);
  }

  @PUT
  @Path("/update/{id}")
  public ResponseEntity<Course> updateCourse(@PathParam("id") int id, @Valid CreateCourseRequestDTO course) {
    Optional<Course> updatedCourse = courseService.updateCourse(id, course);
    return updatedCourse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Course not found"));
  }
}
