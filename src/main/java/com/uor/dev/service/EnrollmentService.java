package com.uor.dev.service;

import com.uor.dev.payload.enrollment.CreateEnrollmentRequestDTO;
import com.uor.dev.payload.enrollment.EnrollmentResponseDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
  List<EnrollmentResponseDTO> getAllEnrollments();

  Optional<EnrollmentResponseDTO> getEnrollmentById(int id);

  boolean deleteEnrollment(int id);
  
  List<EnrollmentResponseDTO> getEnrollmentsByStudentId(int studentId);

  EnrollmentResponseDTO addEnrollment(@Valid CreateEnrollmentRequestDTO enrollmentRequest, int courseId);

  List<EnrollmentResponseDTO> getEnrollmentsByCourseId(int courseId);
}
