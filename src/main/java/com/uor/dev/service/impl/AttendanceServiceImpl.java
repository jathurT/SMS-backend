package com.uor.dev.service.impl;

import com.uor.dev.entity.Attendance;
import com.uor.dev.repo.AttendanceRepository;
import com.uor.dev.repo.SessionRepository;
import com.uor.dev.repo.StudentRepository;
import com.uor.dev.service.AttendanceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AttendanceServiceImpl implements AttendanceService {

  @Inject
  AttendanceRepository attendanceRepository;

  @Inject
  SessionRepository sessionRepository;

  @Inject
  StudentRepository studentRepository;

  @Override
  public List<Attendance> getAllAttendances() {
    List<Attendance> attendances = attendanceRepository.listAll();
    if (attendances.isEmpty()) {
      throw new RuntimeException("No attendances found");
    }
    return attendances;
  }

  @Override
  public List<Attendance> getAttendancesBySessionId(int sessionId) {
    if (sessionRepository.findBySessionId(sessionId).isEmpty()) {
      throw new RuntimeException("Session not found");
    }
    List<Attendance> attendances = attendanceRepository.findBySessionId(sessionId);
    if (attendances.isEmpty()) {
      throw new RuntimeException("No attendances found for session ID: " + sessionId);
    }
    return attendances;
  }

  @Override
  public List<Attendance> getAttendancesByStudentId(int studentId) {
    if (studentRepository.findByStudentId(studentId).isEmpty()) {
      throw new RuntimeException("Student not found");
    }
    List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
    if (attendances.isEmpty()) {
      throw new RuntimeException("No attendances found for student ID: " + studentId);
    }
    return attendances;
  }

  @Override
  @Transactional
  public Attendance addAttendance(int sessionId, int studentId) {
    if (sessionRepository.findBySessionId(sessionId).isEmpty()) {
      throw new RuntimeException("Session not found");
    }
    if (studentRepository.findByStudentId(studentId).isEmpty()) {
      throw new RuntimeException("Student not found");
    }

    Attendance attendance = new Attendance();
    attendance.setSessionId(sessionId);
    attendance.setStudentId(studentId);
    attendanceRepository.persist(attendance);
    return attendance;
  }

  @Override
  public boolean deleteAttendance(int sessionId, int studentId) {
    Optional<Attendance> attendance = attendanceRepository.findBySessionIdAndStudentId(sessionId, studentId);
    if (attendance.isEmpty()) {
      throw new RuntimeException("Attendance not found for session ID: " + sessionId + " and student ID: " + studentId);
    }
    attendanceRepository.delete(attendance.get());
    return true;
  }


}
