package com.uor.dev.service;

import com.uor.dev.entity.Student;
import com.uor.dev.payload.student.CreateStudentRequestDTO;
import com.uor.dev.payload.student.UpdateStudentRequestDTO;

import java.util.List;
import java.util.Optional;

public interface StudentService {

  List<Student> getAllStudents();

  Optional<Student> getStudentById(int id);

  Student addStudent(CreateStudentRequestDTO student);

  Optional<Student> updateStudent(int id, UpdateStudentRequestDTO student);

  boolean deleteStudent(int id);

  Optional<Student> getStudentByEmail(String email);
}
