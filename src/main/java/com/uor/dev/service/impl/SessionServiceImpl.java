package com.uor.dev.service.impl;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Lecturer;
import com.uor.dev.entity.Session;
import com.uor.dev.payload.session.CreateSessionRequestDTO;
import com.uor.dev.repo.CourseRepository;
import com.uor.dev.repo.LecturerRepository;
import com.uor.dev.repo.SessionRepository;
import com.uor.dev.service.SessionService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SessionServiceImpl implements SessionService {

  @Inject
  SessionRepository sessionRepository;

  @Inject
  CourseRepository courseRepository;

  @Inject
  LecturerRepository lecturerRepository;

  @Override
  public List<Session> getAllSessions() {
    List<Session> sessions = sessionRepository.listAll();
    if (sessions.isEmpty()) {
      throw new RuntimeException("No sessions found");
    }
    return sessions;
  }

  @Override
  public Optional<Session> getSessionById(int id) {
    Optional<Session> session = sessionRepository.findBySessionId(id);
    if (session.isEmpty()) {
      throw new RuntimeException("Session not found");
    }
    return session;
  }

  @Override
  public boolean deleteSession(int id) {
    Optional<Session> session = sessionRepository.findBySessionId(id);
    if (session.isEmpty()) {
      throw new RuntimeException("Session not found");
    }
    sessionRepository.delete(session.get());
    return true;
  }

  @Override
  @Transactional
  public Session addSession(int courseId, int lecturerId, CreateSessionRequestDTO createSessionRequestDTO) {
    Optional<Course> course = courseRepository.findByCourseId(courseId);
    if (course.isEmpty()) {
      throw new RuntimeException("Course not found");
    }

    Optional<Lecturer> lecturer = lecturerRepository.findByLecturerId(lecturerId);
    if (lecturer.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }

    Session newSession = Session.builder()
            .course(course.get())
            .lecturer(lecturer.get())
            .date(createSessionRequestDTO.getDate())
            .startTime(createSessionRequestDTO.getStartTime())
            .endTime(createSessionRequestDTO.getEndTime())
            .build();

    sessionRepository.persist(newSession);
    return newSession;
  }

  @Override
  @Transactional
  public Optional<Session> updateSession(int id, CreateSessionRequestDTO session) {
    Optional<Session> existingSession = sessionRepository.findBySessionId(id);
    if (existingSession.isEmpty()) {
      throw new RuntimeException("Session not found");
    }

    Session updatedSession = Session.builder()
            .sessionId(existingSession.get().getSessionId())
            .course(existingSession.get().getCourse())
            .lecturer(existingSession.get().getLecturer())
            .date(session.getDate())
            .startTime(session.getStartTime())
            .endTime(session.getEndTime())
            .build();

    sessionRepository.persist(updatedSession);
    return Optional.of(updatedSession);
  }
}
