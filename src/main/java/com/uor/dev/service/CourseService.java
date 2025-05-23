package com.uor.dev.service;

import com.uor.dev.entity.Course;
import com.uor.dev.payload.course.CreateCourseRequestDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface CourseService {
  List<Course> getAllCourses();

  Optional<Course> getCourseById(int id);

  boolean deleteCourse(int id);

  Course addCourse(@Valid CreateCourseRequestDTO course);

  Optional<Course> updateCourse(int id, @Valid CreateCourseRequestDTO course);
}
