package com.uor.dev.service;

import com.uor.dev.entity.Lecturer;

import java.util.List;
import java.util.Optional;

public interface LectureService {

  List<Lecturer> getAllLecturers();

  Optional<Lecturer> getLecturerById(int id);
}
