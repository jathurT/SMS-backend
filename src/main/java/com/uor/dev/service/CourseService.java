package com.uor.dev.service;

import com.uor.dev.payload.course.CourseAnalyticResponseDTO;
import com.uor.dev.payload.course.CourseResponseDTO;
import com.uor.dev.payload.course.CreateCourseRequestDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface CourseService {
  List<CourseResponseDTO> getAllCourses();

  Optional<CourseResponseDTO> getCourseById(int id);

  boolean deleteCourse(int id);

  CourseResponseDTO addCourse(@Valid CreateCourseRequestDTO course);

  Optional<CourseResponseDTO> updateCourse(int id, @Valid CreateCourseRequestDTO course);

  Optional<CourseResponseDTO> addLecturerToCourse(int courseId, int lecturerId);

  Optional<CourseAnalyticResponseDTO> getCourseDetailsById(int id);

  List<CourseAnalyticResponseDTO> getCourseAnalytics();

  List<CourseResponseDTO> getCoursesByDepartmentId(int departmentId);
}
