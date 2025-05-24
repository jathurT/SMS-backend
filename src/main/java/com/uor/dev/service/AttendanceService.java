package com.uor.dev.service;

import com.uor.dev.entity.Attendance;

import java.util.List;

public interface AttendanceService {
  List<Attendance> getAllAttendances();

  List<Attendance> getAttendancesBySessionId(int sessionId);

  List<Attendance> getAttendancesByStudentId(int studentId);

  Attendance addAttendance(int sessionId, int studentId);

  boolean deleteAttendance(int sessionId, int studentId);
}
