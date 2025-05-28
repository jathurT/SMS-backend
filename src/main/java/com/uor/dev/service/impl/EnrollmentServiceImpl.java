package com.uor.dev.service.impl;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Enrollment;
import com.uor.dev.entity.Student;
import com.uor.dev.payload.enrollment.CreateEnrollmentRequestDTO;
import com.uor.dev.payload.enrollment.EnrollmentResponseDTO;
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
  public List<EnrollmentResponseDTO> getAllEnrollments() {
    List<Enrollment> enrollments = enrollmentRepository.listAll();
    if (enrollments.isEmpty()) {
      throw new RuntimeException("No enrollments found");
    }
    return getEnrollmentResponseDTOS(enrollments);
  }

  @Override
  public Optional<EnrollmentResponseDTO> getEnrollmentById(int id) {
    Optional<Enrollment> enrollment = enrollmentRepository.findByEnrollmentId(id);
    if (enrollment.isEmpty()) {
      throw new RuntimeException("Enrollment not found");
    }
    Enrollment e = enrollment.get();
    EnrollmentResponseDTO dto = EnrollmentResponseDTO.builder()
        .enrollmentId(e.getEnrollmentId())
        .courseId(e.getCourse().getCourseId())
        .courseName(e.getCourse().getCourseName())
        .courseCode(e.getCourse().getCourseCode())
        .studentId(e.getStudent().getStudentId())
        .studentName(e.getStudent().getFirstName() + " " + e.getStudent().getLastName())
        .enrollmentDate(e.getEnrollmentDate().toString())
        .build();
    return Optional.of(dto);
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
  public List<EnrollmentResponseDTO> getEnrollmentsByStudentId(int studentId) {
    Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);
    if (studentOpt.isEmpty()) {
      throw new RuntimeException("Student not found with ID " + studentId);
    }
    Student student = studentOpt.get();

    List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
    if (enrollments.isEmpty()) {
      throw new RuntimeException("No enrollments found for student with ID " + studentId);
    }

    return getEnrollmentResponseDTOS(enrollments);
  }

  @Override
  @Transactional
  public EnrollmentResponseDTO addEnrollment(CreateEnrollmentRequestDTO enrollmentRequest, int courseId) {
    Optional<Course> courseOpt = courseRepository.findByCourseId(courseId);
    if (courseOpt.isEmpty()) {
      throw new RuntimeException("Course not found with ID " + courseId);
    }
    Course course = courseOpt.get();

    Optional<Student> studentOpt = studentRepository.findByStudentId(enrollmentRequest.getStudentId());
    if (studentOpt.isEmpty()) {
      throw new RuntimeException("Student not found with ID " + enrollmentRequest.getStudentId());
    }
    Student student = studentOpt.get();

    if (!enrollmentRequest.getEnrollmentKey().equals(course.getEnrollmentKey())) {
      throw new RuntimeException("Invalid enrollment key for course " + course.getCourseName());
    }

    Enrollment enrollment = Enrollment.builder()
        .course(course)
        .student(student)
        .enrollmentDate(LocalDate.now())
        .build();

    enrollmentRepository.persist(enrollment);

    return EnrollmentResponseDTO.builder()
        .enrollmentId(enrollment.getEnrollmentId())
        .courseId(course.getCourseId())
        .courseName(course.getCourseName())
        .courseCode(course.getCourseCode())
        .studentId(student.getStudentId())
        .studentName(student.getFirstName() + " " + student.getLastName())
        .enrollmentDate(enrollment.getEnrollmentDate().toString())
        .build();
  }

  @Override
  public List<EnrollmentResponseDTO> getEnrollmentsByCourseId(int courseId) {
    Optional<Course> courseOpt = courseRepository.findByCourseId(courseId);
    if (courseOpt.isEmpty()) {
      throw new RuntimeException("Course not found with ID " + courseId);
    }
    Course course = courseOpt.get();

    List<Enrollment> enrollments = enrollmentRepository.findByCourse(course);
    if (enrollments.isEmpty()) {
      throw new RuntimeException("No enrollments found for course with ID " + courseId);
    }

    return getEnrollmentResponseDTOS(enrollments);
  }

  private List<EnrollmentResponseDTO> getEnrollmentResponseDTOS(List<Enrollment> enrollments) {
    List<EnrollmentResponseDTO> response = new ArrayList<>();
    for (Enrollment enrollment : enrollments) {
      EnrollmentResponseDTO dto = EnrollmentResponseDTO.builder()
          .enrollmentId(enrollment.getEnrollmentId())
          .courseId(enrollment.getCourse().getCourseId())
          .courseName(enrollment.getCourse().getCourseName())
          .courseCode(enrollment.getCourse().getCourseCode())
          .studentId(enrollment.getStudent().getStudentId())
          .studentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName())
          .enrollmentDate(enrollment.getEnrollmentDate().toString())
          .build();
      response.add(dto);
    }
    return response;
  }


}
