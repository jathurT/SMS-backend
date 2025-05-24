package com.uor.dev.service.impl;

import com.uor.dev.entity.Attendance;
import com.uor.dev.entity.Session;
import com.uor.dev.entity.Student;
import com.uor.dev.payload.attendance.AttendanceResponseDTO;
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
  public List<AttendanceResponseDTO> getAllAttendances() {
    List<Attendance> attendances = attendanceRepository.listAll();
    if (attendances.isEmpty()) {
      throw new RuntimeException("No attendances found");
    }
    return getAttendanceResponseDTOS(attendances);
  }

  private List<AttendanceResponseDTO> getAttendanceResponseDTOS(List<Attendance> attendances) {
    return attendances.stream().map(attendance -> AttendanceResponseDTO.builder()
            .studentId(attendance.getStudentId())
            .studentName(attendance.getStudent().getFirstName() + " " + attendance.getStudent().getLastName())
            .sessionId(attendance.getSessionId())
            .sessionName(attendance.getSession().getCourse().getCourseName())
            .lecturerName(attendance.getSession().getLecturer().getFirstName() + " " + attendance.getSession().getLecturer().getLastName())
            .date(String.valueOf(attendance.getSession().getDate()))
            .startTime(String.valueOf(attendance.getSession().getStartTime()))
            .endTime(String.valueOf(attendance.getSession().getEndTime()))
            .build()).toList();
  }

  @Override
  public List<AttendanceResponseDTO> getAttendancesBySessionId(int sessionId) {
    if (sessionRepository.findBySessionId(sessionId).isEmpty()) {
      throw new RuntimeException("Session not found");
    }
    List<Attendance> attendances = attendanceRepository.findBySessionId(sessionId);
    if (attendances.isEmpty()) {
      throw new RuntimeException("No attendances found for session ID: " + sessionId);
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

    if (attendanceRepository.existsBySessionIdAndStudentId(sessionId, studentId)) {
      throw new RuntimeException("Attendance already exists for session ID: " + sessionId + " and student ID: " + studentId);
    }

    Attendance attendance = new Attendance();
    attendance.setSessionId(sessionId);
    attendance.setStudentId(studentId);
    attendanceRepository.persist(attendance);

    Session session = sessionOpt.get();
    Student student = studentOpt.get();

    return AttendanceResponseDTO.builder()
            .studentId(studentId)
            .studentName(student.getFirstName() + " " + student.getLastName())
            .sessionId(sessionId)
            .sessionName(session.getCourse().getCourseName())
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
}
