package com.uor.dev.service;

import com.uor.dev.payload.attendance.AttendanceResponseDTO;

import java.util.List;

public interface AttendanceService {
  List<AttendanceResponseDTO> getAllAttendances();

  List<AttendanceResponseDTO> getAttendancesBySessionId(int sessionId);

  List<AttendanceResponseDTO> getAttendancesByStudentId(int studentId);

  AttendanceResponseDTO addAttendance(int sessionId, int studentId);

  boolean deleteAttendance(int sessionId, int studentId);
}
