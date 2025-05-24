package com.uor.dev.service.impl;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Lecturer;
import com.uor.dev.entity.Session;
import com.uor.dev.payload.session.CreateSessionRequestDTO;
import com.uor.dev.payload.session.SessionResponseDTO;
import com.uor.dev.payload.session.UpdateSessionRequestDTO;
import com.uor.dev.payload.student.StudentResponseDTO;
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
  public List<SessionResponseDTO> getAllSessions() {
    List<Session> sessions = sessionRepository.listAll();
    if (sessions.isEmpty()) {
      throw new RuntimeException("No sessions found");
    }
    return sessions.stream().map(session -> {
      List<StudentResponseDTO> attendedStudents = session.getStudents().stream()
              .map(student -> StudentResponseDTO.basicBuilder()
                      .firstName(student.getFirstName())
                      .email(student.getEmail())
                      .phoneNumber(student.getPhoneNumber())
                      .build())
              .toList();

      return SessionResponseDTO.fullBuilder()
              .sessionId(session.getSessionId())
              .courseName(session.getCourse().getCourseName())
              .courseCode(session.getCourse().getCourseCode())
              .lecturerName(session.getLecturer().getFirstName() + " " + session.getLecturer().getLastName())
              .date(String.valueOf(session.getDate()))
              .startTime(String.valueOf(session.getStartTime()))
              .endTime(String.valueOf(session.getEndTime()))
              .studentsAttended(attendedStudents)
              .build();
    }).toList();
  }

  @Override
  public Optional<SessionResponseDTO> getSessionById(int id) {
    Optional<Session> session = sessionRepository.findBySessionId(id);
    if (session.isEmpty()) {
      throw new RuntimeException("Session not found");
    }
    SessionResponseDTO sessionResponseDTO = SessionResponseDTO.basicBuilder()
            .sessionId(session.get().getSessionId())
            .courseName(session.get().getCourse().getCourseName())
            .courseCode(session.get().getCourse().getCourseCode())
            .lecturerName(session.get().getLecturer().getFirstName() + " " + session.get().getLecturer().getLastName())
            .date(String.valueOf(session.get().getDate()))
            .startTime(String.valueOf(session.get().getStartTime()))
            .endTime(String.valueOf(session.get().getEndTime()))
            .build();
    return Optional.of(sessionResponseDTO);
  }

  @Override
  @Transactional
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
  public SessionResponseDTO addSession(int courseId, int lecturerId, CreateSessionRequestDTO createSessionRequestDTO) {
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

    return SessionResponseDTO.basicBuilder()
            .sessionId(newSession.getSessionId())
            .courseName(newSession.getCourse().getCourseName())
            .courseCode(newSession.getCourse().getCourseCode())
            .lecturerName(newSession.getLecturer().getFirstName() + " " + newSession.getLecturer().getLastName())
            .date(String.valueOf(newSession.getDate()))
            .startTime(String.valueOf(newSession.getStartTime()))
            .endTime(String.valueOf(newSession.getEndTime()))
            .build();
  }

  @Override
  @Transactional
  public Optional<SessionResponseDTO> updateSession(int id, UpdateSessionRequestDTO session) {
    Optional<Session> existingSession = sessionRepository.findBySessionId(id);
    if (existingSession.isEmpty()) {
      throw new RuntimeException("Session not found");
    }

    Session sessionToUpdate = existingSession.get();
    Optional<Course> course = courseRepository.findByCourseId(Integer.parseInt(session.getCourseId()));
    if (course.isEmpty()) {
      throw new RuntimeException("Course not found");
    }

    Optional<Lecturer> lecturer = lecturerRepository.findByLecturerId(Integer.parseInt(session.getLecturerId()));
    if (lecturer.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }

    sessionToUpdate.setCourse(course.get());
    sessionToUpdate.setLecturer(lecturer.get());
    sessionToUpdate.setDate(session.getDate());
    sessionToUpdate.setStartTime(session.getStartTime());
    sessionToUpdate.setEndTime(session.getEndTime());

    sessionRepository.persist(sessionToUpdate);

    SessionResponseDTO response = SessionResponseDTO.basicBuilder()
            .sessionId(sessionToUpdate.getSessionId())
            .courseName(sessionToUpdate.getCourse().getCourseName())
            .courseCode(sessionToUpdate.getCourse().getCourseCode())
            .lecturerName(sessionToUpdate.getLecturer().getFirstName() + " " + sessionToUpdate.getLecturer().getLastName())
            .date(String.valueOf(sessionToUpdate.getDate()))
            .startTime(String.valueOf(sessionToUpdate.getStartTime()))
            .endTime(String.valueOf(sessionToUpdate.getEndTime()))
            .build();

    return Optional.of(response);
  }
}
