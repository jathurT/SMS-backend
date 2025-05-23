package com.uor.dev.repo;

import com.uor.dev.entity.Lecturer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class LecturerRepository implements PanacheRepository<Lecturer> {

  public Optional<Lecturer> findByLecturerId(int id) {
    return find("lecturerId", id).firstResultOptional();
  }
}
