package com.uor.dev.service;

import com.uor.dev.payload.student.CreateStudentRequestDTO;
import com.uor.dev.payload.student.StudentResponseDTO;
import com.uor.dev.payload.student.UpdateStudentRequestDTO;

import java.util.List;
import java.util.Optional;

public interface StudentService {

  List<StudentResponseDTO> getAllStudents();

  Optional<StudentResponseDTO> getStudentById(int id);

  StudentResponseDTO addStudent(CreateStudentRequestDTO student);

  Optional<StudentResponseDTO> updateStudent(int id, UpdateStudentRequestDTO student);

  boolean deleteStudent(int id);

  Optional<StudentResponseDTO> getStudentByEmail(String email);
}
