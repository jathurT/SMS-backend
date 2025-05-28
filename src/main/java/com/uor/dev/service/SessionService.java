package com.uor.dev.service;

import com.uor.dev.payload.session.CreateSessionRequestDTO;
import com.uor.dev.payload.session.SessionResponseDTO;
import com.uor.dev.payload.session.UpdateSessionRequestDTO;

import java.util.List;
import java.util.Optional;

public interface SessionService {
  List<SessionResponseDTO> getAllSessions();

  Optional<SessionResponseDTO> getSessionById(int id);

  boolean deleteSession(int id);

  SessionResponseDTO addSession(int courseId, int lecturerId, CreateSessionRequestDTO createSessionRequestDTO);

  Optional<SessionResponseDTO> updateSession(int id, UpdateSessionRequestDTO session);

  List<SessionResponseDTO> getSessionsByCourseId(int courseId);
}
