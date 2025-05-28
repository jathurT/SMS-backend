package com.uor.dev.service.impl;

import com.uor.dev.entity.*;
import com.uor.dev.payload.attendance.AttendanceResponseDTO;
import com.uor.dev.payload.student.StudentResponseDTO;
import com.uor.dev.repo.AttendanceRepository;
import com.uor.dev.repo.EnrollmentRepository;
import com.uor.dev.repo.SessionRepository;
import com.uor.dev.repo.StudentRepository;
import com.uor.dev.service.AttendanceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class AttendanceServiceImpl implements AttendanceService {

  @Inject
  AttendanceRepository attendanceRepository;

  @Inject
  SessionRepository sessionRepository;

  @Inject
  StudentRepository studentRepository;

  @Inject
  EnrollmentRepository enrollmentRepository;

  @Override
  public List<AttendanceResponseDTO> getAllAttendances() {
    List<Attendance> attendances = attendanceRepository.listAll();
    if (attendances.isEmpty()) {
      throw new RuntimeException("No attendances found");
    }
    return getAttendanceResponseDTOS(attendances);
  }

  private List<AttendanceResponseDTO> getAttendanceResponseDTOS(List<Attendance> attendances) {
    return attendances.stream()
            .map(attendance -> {
              // Safely fetch related entities
              Optional<Student> studentOpt = studentRepository.findByStudentId(attendance.getStudentId());
              Optional<Session> sessionOpt = sessionRepository.findBySessionId(attendance.getSessionId());

              if (studentOpt.isEmpty() || sessionOpt.isEmpty()) {
                return null; // Skip invalid records
              }

              Student student = studentOpt.get();
              Session session = sessionOpt.get();

              return AttendanceResponseDTO.builder()
                      .studentId(attendance.getStudentId())
                      .studentName(student.getFirstName() + " " + student.getLastName())
                      .sessionId(attendance.getSessionId())
                      .sessionName(session.getCourse().getCourseName())
                      .lecturerName(session.getLecturer().getFirstName() + " " + session.getLecturer().getLastName())
                      .date(String.valueOf(session.getDate()))
                      .startTime(String.valueOf(session.getStartTime()))
                      .endTime(String.valueOf(session.getEndTime()))
                      .build();
            })
            .filter(Objects::nonNull) // Remove null records
            .collect(Collectors.toList());
  }

  @Override
  public List<AttendanceResponseDTO> getAttendancesBySessionId(int sessionId) {
    Optional<Session> sessionOpt = sessionRepository.findBySessionId(sessionId);
    if (sessionOpt.isEmpty()) {
      throw new RuntimeException("Session not found");
    }

    List<Attendance> attendances = attendanceRepository.findBySessionId(sessionId);
    // Return empty list if no attendances found (this is normal for new sessions)
    if (attendances.isEmpty()) {
      return List.of();
    }
    return getAttendanceResponseDTOS(attendances);
  }

  @Override
  public List<AttendanceResponseDTO> getAttendancesByStudentId(int studentId) {
    if (studentRepository.findByStudentId(studentId).isEmpty()) {
      throw new RuntimeException("Student not found");
    }
    List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
    if (attendances.isEmpty()) {
      throw new RuntimeException("No attendances found for student ID: " + studentId);
    }
    return getAttendanceResponseDTOS(attendances);
  }

  @Override
  @Transactional
  public AttendanceResponseDTO addAttendance(int sessionId, int studentId) {
    Optional<Session> sessionOpt = sessionRepository.findBySessionId(sessionId);
    Optional<Student> studentOpt = studentRepository.findByStudentId(studentId);

    if (sessionOpt.isEmpty()) {
      throw new RuntimeException("Session not found");
    }
    if (studentOpt.isEmpty()) {
      throw new RuntimeException("Student not found");
    }

    Session session = sessionOpt.get();
    Student student = studentOpt.get();
    Course course = session.getCourse();

    // Check if student is enrolled in the course
    if (!enrollmentRepository.existsByCourseIdAndStudentId(course.getCourseId(), studentId)) {
      throw new RuntimeException("Student is not enrolled in the course for this session");
    }

    // Check if attendance already exists
    if (attendanceRepository.existsBySessionIdAndStudentId(sessionId, studentId)) {
      throw new RuntimeException("Attendance already exists for session ID: " + sessionId + " and student ID: " + studentId);
    }

    // Create new attendance record
    Attendance attendance = new Attendance();
    attendance.setSessionId(sessionId);
    attendance.setStudentId(studentId);
    attendanceRepository.persist(attendance);

    // Return the attendance response DTO
    return AttendanceResponseDTO.builder()
            .studentId(studentId)
            .studentName(student.getFirstName() + " " + student.getLastName())
            .sessionId(sessionId)
            .sessionName(course.getCourseName())
            .lecturerName(session.getLecturer().getFirstName() + " " + session.getLecturer().getLastName())
            .date(String.valueOf(session.getDate()))
            .startTime(String.valueOf(session.getStartTime()))
            .endTime(String.valueOf(session.getEndTime()))
            .build();
  }

  @Override
  @Transactional
  public boolean deleteAttendance(int sessionId, int studentId) {
    Optional<Attendance> attendance = attendanceRepository.findBySessionIdAndStudentId(sessionId, studentId);
    if (attendance.isEmpty()) {
      throw new RuntimeException("Attendance not found for session ID: " + sessionId + " and student ID: " + studentId);
    }
    attendanceRepository.delete(attendance.get());
    return true;
  }

  @Override
  public List<StudentResponseDTO> getNonAttendingStudents(int sessionId) {
    Optional<Session> sessionOpt = sessionRepository.findBySessionId(sessionId);
    if (sessionOpt.isEmpty()) {
      throw new RuntimeException("Session not found");
    }

    Session session = sessionOpt.get();
    Course course = session.getCourse();

    List<Student> enrolledStudents = enrollmentRepository.findStudentsByCourseId(course.getCourseId());

    List<Attendance> attendances = attendanceRepository.findBySessionId(sessionId);

    List<Integer> attendedStudentIds = attendances.stream()
            .map(Attendance::getStudentId)
            .toList();

    return enrolledStudents.stream()
            .filter(student -> !attendedStudentIds.contains(student.getStudentId()))
            .map(student -> StudentResponseDTO.partialBuilder()
                    .studentId(student.getStudentId())
                    .firstName(student.getFirstName())
                    .email(student.getEmail())
                    .phoneNumber(student.getPhoneNumber())
                    .build())
            .collect(Collectors.toList());
  }
}
