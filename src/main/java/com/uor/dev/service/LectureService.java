package com.uor.dev.service;

import com.uor.dev.payload.lecturer.CreateLecturerRequestDTO;
import com.uor.dev.payload.lecturer.LectureCourseResponseDTO;
import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import com.uor.dev.payload.lecturer.UpdateLecturerRequestDTO;

import java.util.List;
import java.util.Optional;

public interface LectureService {

  List<LecturerResponseDTO> getAllLecturers();

  Optional<LecturerResponseDTO> getLecturerById(int id);

  LecturerResponseDTO addLecturer(CreateLecturerRequestDTO lecturer);

  Optional<LecturerResponseDTO> updateLecturer(int id, UpdateLecturerRequestDTO lecturer);

  boolean deleteLecturer(int id);

  Optional<LectureCourseResponseDTO> getLecturerDetails(int id);
}
