package com.uor.dev.repo;

import com.uor.dev.entity.Session;
import com.uor.dev.payload.student.StudentResponseDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SessionRepository implements PanacheRepository<Session> {

  public Optional<Session> findBySessionId(int id) {
    return find("sessionId", id).firstResultOptional();
  }


}
