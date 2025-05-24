package com.uor.dev.service;

import com.uor.dev.entity.Session;
import com.uor.dev.payload.session.CreateSessionRequestDTO;

import java.util.List;
import java.util.Optional;

public interface SessionService {
  List<Session> getAllSessions();

  Optional<Session> getSessionById(int id);

  boolean deleteSession(int id);

  Session addSession(int courseId, int lecturerId, CreateSessionRequestDTO createSessionRequestDTO);

  Optional<Session> updateSession(int id, CreateSessionRequestDTO session);
}
