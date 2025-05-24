package com.uor.dev.service.impl;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Enrollment;
import com.uor.dev.entity.Student;
import com.uor.dev.repo.CourseRepository;
import com.uor.dev.repo.EnrollmentRepository;
import com.uor.dev.repo.StudentRepository;
import com.uor.dev.service.EnrollmentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EnrollmentServiceImpl implements EnrollmentService {
  @Inject
  EnrollmentRepository enrollmentRepository;

  @Inject
  CourseRepository courseRepository;

  @Inject
  StudentRepository studentRepository;

  @Override
  public List<Enrollment> getAllEnrollments() {
    List<Enrollment> enrollments = new ArrayList<>(enrollmentRepository.listAll());
    if (enrollments.isEmpty()) {
      throw new RuntimeException("No enrollments found");
    }
    return enrollments;
  }

  @Override
  public Optional<Enrollment> getEnrollmentById(int id) {
    Optional<Enrollment> enrollment = enrollmentRepository.findByEnrollmentId(id);
    if (enrollment.isEmpty()) {
      throw new RuntimeException("Enrollment not found");
    }
    return enrollment;
  }

  @Override
  @Transactional
  public boolean deleteEnrollment(int id) {
    Optional<Enrollment> enrollment = enrollmentRepository.findByEnrollmentId(id);
    if (enrollment.isEmpty()) {
      throw new RuntimeException("Enrollment not found");
    }
    enrollmentRepository.delete(enrollment.get());
    return true;
  }

  @Override
  @Transactional
  public Enrollment addEnrollment(int courseId, int studentId) {
    Course course = courseRepository.findByCourseId(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

    Student student = studentRepository.findByStudentId(studentId)
            .orElseThrow(() -> new RuntimeException("Student not found"));

    Enrollment enrollment = Enrollment.builder()
            .course(course)
            .student(student)
            .enrollmentDate(LocalDate.now())
            .build();
    enrollmentRepository.persist(enrollment);
    return enrollment;
  }

  @Override
  public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
    List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
    if (enrollments.isEmpty()) {
      throw new RuntimeException("No enrollments found for student with ID " + studentId);
    }
    return enrollments;
  }


}
