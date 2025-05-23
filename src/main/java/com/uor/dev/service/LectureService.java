package com.uor.dev.service;

import com.uor.dev.entity.Lecturer;
import com.uor.dev.payload.lecturer.CreateLecturerRequestDTO;
import com.uor.dev.payload.lecturer.UpdateLecturerRequestDTO;

import java.util.List;
import java.util.Optional;

public interface LectureService {

  List<Lecturer> getAllLecturers();

  Optional<Lecturer> getLecturerById(int id);

  Lecturer addLecturer(CreateLecturerRequestDTO lecturer);

  Optional<Lecturer> updateLecturer(int id, UpdateLecturerRequestDTO lecturer);

  boolean deleteLecturer(int id);
}
