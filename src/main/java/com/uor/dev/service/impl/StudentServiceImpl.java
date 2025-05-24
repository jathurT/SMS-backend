package com.uor.dev.service.impl;

import com.uor.dev.entity.Student;
import com.uor.dev.payload.student.CreateStudentRequestDTO;
import com.uor.dev.payload.student.StudentResponseDTO;
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
  public List<StudentResponseDTO> getAllStudents() {
    List<Student> students = new ArrayList<>(studentRepository.listAll());
    if (students.isEmpty()) {
      throw new RuntimeException("No students found");
    }
    List<StudentResponseDTO> studentResponseDTOs = new ArrayList<>();
    for (Student student : students) {
      StudentResponseDTO studentResponseDTO = StudentResponseDTO.builder()
              .studentId(student.getStudentId())
              .firstName(student.getFirstName())
              .lastName(student.getLastName())
              .email(student.getEmail())
              .phoneNumber(student.getPhoneNumber())
              .dateOfBirth(String.valueOf(student.getDateOfBirth()))
              .build();
      studentResponseDTOs.add(studentResponseDTO);
    }
    return studentResponseDTOs;
  }

  @Override
  public Optional<StudentResponseDTO> getStudentById(int id) {
    Optional<Student> student = studentRepository.findByStudentId(id);
    return fetchStudentByFeature(student);
  }

  @Override
  @Transactional
  public StudentResponseDTO addStudent(CreateStudentRequestDTO student) {
    if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
      throw new RuntimeException("Student with this email already exists");
    }
    Student newStudent = Student.builder()
            .firstName(student.getFirstName())
            .lastName(student.getLastName())
            .email(student.getEmail())
            .phoneNumber(student.getPhoneNumber())
            .address(student.getAddress())
            .dateOfBirth(student.getDateOfBirth())
            .build();
    studentRepository.persist(newStudent);
    return StudentResponseDTO.builder()
            .studentId(newStudent.getStudentId())
            .firstName(newStudent.getFirstName())
            .lastName(newStudent.getLastName())
            .email(newStudent.getEmail())
            .phoneNumber(newStudent.getPhoneNumber())
            .dateOfBirth(String.valueOf(newStudent.getDateOfBirth()))
            .build();
  }


  @Override
  @Transactional
  public Optional<StudentResponseDTO> updateStudent(int id, UpdateStudentRequestDTO student) {
    Optional<Student> existingStudent = studentRepository.findByStudentId(id);
    if (existingStudent.isEmpty()) {
      throw new RuntimeException("Student not found");
    }
    Student studentToUpdate = existingStudent.get();

    if (studentRepository.findByEmail(student.getEmail()).isPresent() &&
            !studentToUpdate.getEmail().equals(student.getEmail())) {
      throw new RuntimeException("Student with this email already exists");
    }

    studentToUpdate.setFirstName(student.getFirstName());
    studentToUpdate.setLastName(student.getLastName());
    studentToUpdate.setEmail(student.getEmail());
    studentToUpdate.setPhoneNumber(student.getPhoneNumber());
    studentToUpdate.setAddress(student.getAddress());
    studentToUpdate.setDateOfBirth(student.getDateOfBirth());
    studentRepository.persist(studentToUpdate);

    StudentResponseDTO response = StudentResponseDTO.builder()
            .studentId(studentToUpdate.getStudentId())
            .firstName(studentToUpdate.getFirstName())
            .lastName(studentToUpdate.getLastName())
            .email(studentToUpdate.getEmail())
            .phoneNumber(studentToUpdate.getPhoneNumber())
            .dateOfBirth(String.valueOf(studentToUpdate.getDateOfBirth()))
            .build();

    return Optional.of(response);
  }

  @Override
  @Transactional
  public boolean deleteStudent(int id) {
    Optional<Student> student = studentRepository.findByStudentId(id);
    if (student.isEmpty()) {
      throw new RuntimeException("Student not found");
    }
    studentRepository.delete(student.get());
    return true;
  }

  @Override
  public Optional<StudentResponseDTO> getStudentByEmail(String email) {
    Optional<Student> student = studentRepository.findByEmail(email);
    return fetchStudentByFeature(student);
  }

  private Optional<StudentResponseDTO> fetchStudentByFeature(Optional<Student> student) {
    if (student.isEmpty()) {
      throw new RuntimeException("Student not found");
    }
    StudentResponseDTO studentResponseDTO = StudentResponseDTO.builder()
            .studentId(student.get().getStudentId())
            .firstName(student.get().getFirstName())
            .lastName(student.get().getLastName())
            .email(student.get().getEmail())
            .phoneNumber(student.get().getPhoneNumber())
            .dateOfBirth(String.valueOf(student.get().getDateOfBirth()))
            .build();
    return Optional.of(studentResponseDTO);
  }
}
