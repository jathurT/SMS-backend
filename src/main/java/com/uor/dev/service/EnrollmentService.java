package com.uor.dev.service;

import com.uor.dev.entity.Enrollment;
import com.uor.dev.payload.enrollment.EnrollmentResponseDTO;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
  List<EnrollmentResponseDTO> getAllEnrollments();

  Optional<EnrollmentResponseDTO> getEnrollmentById(int id);

  boolean deleteEnrollment(int id);

  EnrollmentResponseDTO addEnrollment(int courseId, int studentId);

  List<EnrollmentResponseDTO> getEnrollmentsByStudentId(int studentId);
}
