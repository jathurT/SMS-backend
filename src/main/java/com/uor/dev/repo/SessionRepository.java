package com.uor.dev.repo;

import com.uor.dev.entity.Session;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SessionRepository implements PanacheRepository<Session> {
  public Optional<Session> findBySessionId(int id) {
    return find("sessionId", id).firstResultOptional();
  }

  public List<Session> findSessionsByStudentId(int id) {
    if (id <= 0) {
      return List.of();
    }
    return find("select s from Session s join s.students st where st.studentId = ?1", id).list();
  }

  public List<Session> findByCourseId(int courseId) {
    if (courseId <= 0) {
      return List.of();
    }
    return find("select s from Session s where s.course.courseId = ?1", courseId).list();
  }
}
