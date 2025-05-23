package com.uor.dev.service.impl;

import com.uor.dev.entity.Department;
import com.uor.dev.entity.Lecturer;
import com.uor.dev.payload.lecturer.CreateLecturerRequestDTO;
import com.uor.dev.payload.lecturer.UpdateLecturerRequestDTO;
import com.uor.dev.repo.DepartmentRepository;
import com.uor.dev.repo.LecturerRepository;
import com.uor.dev.service.LectureService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LectureServiceImpl implements LectureService {

  @Inject
  LecturerRepository lecturerRepository;

  @Inject
  DepartmentRepository departmentRepository;

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

  @Override
  @Transactional
  public Lecturer addLecturer(CreateLecturerRequestDTO lecturer) {
    Optional<Lecturer> existingLecturer = lecturerRepository.findByEmail(lecturer.getEmail());
    if (existingLecturer.isPresent()) {
      throw new RuntimeException("Lecturer with email " + lecturer.getEmail() + " already exists");
    }
    Integer departmentId = lecturer.getDepartmentId();
    Optional<Department> department = departmentRepository.findByDepartmentId(departmentId);
    if (department.isEmpty()) {
      throw new RuntimeException("Department with ID " + departmentId + " not found");
    }
    Lecturer newLecturer = new Lecturer();
    newLecturer.setFirstName(lecturer.getFirstName());
    newLecturer.setLastName(lecturer.getLastName());
    newLecturer.setEmail(lecturer.getEmail());
    newLecturer.setPhoneNumber(lecturer.getPhoneNumber());
    newLecturer.setAddress(lecturer.getAddress());
    newLecturer.setDateOfBirth(lecturer.getDateOfBirth());
    newLecturer.setDepartment(department.get());
    lecturerRepository.persist(newLecturer);
    return newLecturer;
  }

  @Override
  @Transactional
  public Optional<Lecturer> updateLecturer(int id, UpdateLecturerRequestDTO lecturer) {
    Optional<Lecturer> existingLecturer = lecturerRepository.findByLecturerId(id);
    if (existingLecturer.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }
    Integer departmentId = lecturer.getDepartmentId();
    Optional<Department> department = departmentRepository.findByDepartmentId(departmentId);
    if (department.isEmpty()) {
      throw new RuntimeException("Department with ID " + departmentId + " not found");
    }
    Lecturer updatedLecturer = existingLecturer.get();
    updatedLecturer.setFirstName(lecturer.getFirstName());
    updatedLecturer.setLastName(lecturer.getLastName());
    updatedLecturer.setEmail(lecturer.getEmail());
    updatedLecturer.setPhoneNumber(lecturer.getPhoneNumber());
    updatedLecturer.setAddress(lecturer.getAddress());
    updatedLecturer.setDateOfBirth(lecturer.getDateOfBirth());
    updatedLecturer.setDepartment(department.get());
    lecturerRepository.persist(updatedLecturer);
    return Optional.of(updatedLecturer);
  }

  @Override
  public boolean deleteLecturer(int id) {
    Optional<Lecturer> lecturer = lecturerRepository.findByLecturerId(id);
    if (lecturer.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }
    lecturerRepository.delete(lecturer.get());
    return true;
  }
}
