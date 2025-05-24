package com.uor.dev.service.impl;

import com.uor.dev.entity.Student;
import com.uor.dev.payload.student.CreateStudentRequestDTO;
import com.uor.dev.payload.student.UpdateStudentRequestDTO;
import com.uor.dev.repo.StudentRepository;
import com.uor.dev.service.StudentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class StudentServiceImpl implements StudentService {

  @Inject
  StudentRepository studentRepository;

  @Override
  public List<Student> getAllStudents() {
    List<Student> students = new ArrayList<>(studentRepository.listAll());
    if (students.isEmpty()) {
      throw new RuntimeException("No students found");
    }
    return students;
  }

  @Override
  public Optional<Student> getStudentById(int id) {
    Optional<Student> student = studentRepository.findByStudentId(id);
    if (student.isEmpty()) {
      throw new RuntimeException("Student not found");
    }
    return student;
  }

  @Override
  @Transactional
  public Student addStudent(CreateStudentRequestDTO student) {
    Optional<Student> existingStudent = studentRepository.findByEmail(student.getEmail());
    if (existingStudent.isPresent()) {
      throw new RuntimeException("Student with email " + student.getEmail() + " already exists");
    }

    Student newStudent = Student.builder()
            .firstName(student.getFirstName())
            .lastName(student.getLastName())
            .email(student.getEmail())
            .phoneNumber(student.getPhoneNumber())
            .dateOfBirth(student.getDateOfBirth())
            .build();

    studentRepository.persist(newStudent);
    return newStudent;
  }


  @Override
  public Optional<Student> updateStudent(int id, UpdateStudentRequestDTO student) {
    Optional<Student> existingStudent = studentRepository.findByStudentId(id);
    if (existingStudent.isEmpty()) {
      throw new RuntimeException("Student not found");
    }
    if (!existingStudent.get().getEmail().equals(student.getEmail())) {
      Optional<Student> studentWithSameEmail = studentRepository.findByEmail(student.getEmail());
      if (studentWithSameEmail.isPresent()) {
        throw new RuntimeException("Student with email " + student.getEmail() + " already exists");
      }
    }
    Student updatedStudent = Student.builder()
            .studentId(existingStudent.get().getStudentId())
            .firstName(student.getFirstName())
            .lastName(student.getLastName())
            .email(student.getEmail())
            .phoneNumber(student.getPhoneNumber())
            .address(student.getAddress())
            .dateOfBirth(student.getDateOfBirth())
            .enrollments(existingStudent.get().getEnrollments())
            .sessions(existingStudent.get().getSessions())
            .build();
    studentRepository.persist(updatedStudent);
    return Optional.of(updatedStudent);
  }

  @Override
  public boolean deleteStudent(int id) {
    Optional<Student> student = studentRepository.findByStudentId(id);
    if (student.isEmpty()) {
      throw new RuntimeException("Student not found");
    }
    studentRepository.delete(student.get());
    return true;
  }

  @Override
  public Optional<Student> getStudentByEmail(String email) {
    Optional<Student> student = studentRepository.findByEmail(email);
    if (student.isEmpty()) {
      throw new RuntimeException("Student not found");
    }
    return student;
  }
}
