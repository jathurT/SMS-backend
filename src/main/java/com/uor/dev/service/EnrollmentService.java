package com.uor.dev.service;

import com.uor.dev.entity.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
  List<Enrollment> getAllEnrollments();

  Optional<Enrollment> getEnrollmentById(int id);

  boolean deleteEnrollment(int id);

  Enrollment addEnrollment(int courseId, int studentId);

  List<Enrollment> getEnrollmentsByStudentId(int studentId);
}
