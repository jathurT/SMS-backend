package com.uor.dev.service.impl;

import com.uor.dev.entity.Lecturer;
import com.uor.dev.repo.LecturerRepository;
import com.uor.dev.service.LectureService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LectureServiceImpl implements LectureService {

  @Inject
  LecturerRepository lecturerRepository;

  @Override
  public List<Lecturer> getAllLecturers() {
    List<Lecturer> lecturers = new ArrayList<>(lecturerRepository.listAll());
    if (lecturers.isEmpty()) {
      throw new RuntimeException("No lecturers found");
    }
    return lecturers;
  }

  @Override
  public Optional<Lecturer> getLecturerById(int id) {
    Optional<Lecturer> lecturer = lecturerRepository.findByLecturerId(id);
    if (lecturer.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }
    return lecturer;
  }
}
