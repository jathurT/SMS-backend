package com.uor.dev.repo;

import com.uor.dev.entity.Attendance;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AttendanceRepository implements PanacheRepository<Attendance> {


  public List<Attendance> findBySessionId(int sessionId) {
    return find("session.sessionId", sessionId).list();
  }

  public List<Attendance> findByStudentId(int studentId) {
    return find("student.studentId", studentId).list();
  }

  public Optional<Attendance> findBySessionIdAndStudentId(int sessionId, int studentId) {
    return find("session.sessionId = ?1 and student.studentId = ?2", sessionId, studentId).firstResultOptional();
  }
}
