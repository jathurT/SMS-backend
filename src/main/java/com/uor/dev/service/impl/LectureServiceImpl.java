package com.uor.dev.service.impl;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Department;
import com.uor.dev.entity.Lecturer;
import com.uor.dev.payload.course.CourseResponseDTO;
import com.uor.dev.payload.lecturer.CreateLecturerRequestDTO;
import com.uor.dev.payload.lecturer.LectureCourseResponseDTO;
import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import com.uor.dev.payload.lecturer.UpdateLecturerRequestDTO;
import com.uor.dev.repo.CourseRepository;
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

  @Inject
  CourseRepository courseRepository;

  @Override
  public List<LecturerResponseDTO> getAllLecturers() {
    List<Lecturer> lecturers = new ArrayList<>(lecturerRepository.listAll());
    if (lecturers.isEmpty()) {
      throw new RuntimeException("No lecturers found");
    }
    List<LecturerResponseDTO> lecturerResponseDTOs = new ArrayList<>();
    for (Lecturer lecturer : lecturers) {
      LecturerResponseDTO lecturerResponseDTO = LecturerResponseDTO.builder()
          .lecturerId(lecturer.getLecturerId())
          .firstName(lecturer.getFirstName())
          .lastName(lecturer.getLastName())
          .email(lecturer.getEmail())
          .phoneNumber(lecturer.getPhoneNumber())
          .address(lecturer.getAddress())
          .dateOfBirth(String.valueOf(lecturer.getDateOfBirth()))
          .departmentName(lecturer.getDepartment().getDepartmentName())
          .build();
      lecturerResponseDTOs.add(lecturerResponseDTO);
    }
    return lecturerResponseDTOs;

  }

  @Override
  public Optional<LecturerResponseDTO> getLecturerById(int id) {
    Optional<Lecturer> lecturer = lecturerRepository.findByLecturerId(id);
    if (lecturer.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }
    LecturerResponseDTO lecturerResponseDTO = LecturerResponseDTO.builder()
        .lecturerId(lecturer.get().getLecturerId())
        .firstName(lecturer.get().getFirstName())
        .lastName(lecturer.get().getLastName())
        .email(lecturer.get().getEmail())
        .phoneNumber(lecturer.get().getPhoneNumber())
        .address(lecturer.get().getAddress())
        .dateOfBirth(String.valueOf(lecturer.get().getDateOfBirth()))
        .departmentName(lecturer.get().getDepartment().getDepartmentName())
        .build();
    return Optional.of(lecturerResponseDTO);

  }

  @Override
  @Transactional
  public LecturerResponseDTO addLecturer(CreateLecturerRequestDTO lecturer) {
    Optional<Lecturer> existingLecturer = lecturerRepository.findByEmail(lecturer.getEmail());
    if (existingLecturer.isPresent()) {
      throw new RuntimeException("Lecturer with email " + lecturer.getEmail() + " already exists");
    }
    Integer departmentId = lecturer.getDepartmentId();
    Optional<Department> department = departmentRepository.findByDepartmentId(departmentId);
    if (department.isEmpty()) {
      throw new RuntimeException("Department with ID " + departmentId + " not found");
    }
    Lecturer newLecturer = Lecturer.builder()
        .firstName(lecturer.getFirstName())
        .lastName(lecturer.getLastName())
        .email(lecturer.getEmail())
        .phoneNumber(lecturer.getPhoneNumber())
        .address(lecturer.getAddress())
        .dateOfBirth(lecturer.getDateOfBirth())
        .department(department.get())
        .build();
    lecturerRepository.persist(newLecturer);

    return LecturerResponseDTO.builder()
        .lecturerId(newLecturer.getLecturerId())
        .firstName(newLecturer.getFirstName())
        .lastName(newLecturer.getLastName())
        .email(newLecturer.getEmail())
        .phoneNumber(newLecturer.getPhoneNumber())
        .address(newLecturer.getAddress())
        .dateOfBirth(String.valueOf(newLecturer.getDateOfBirth()))
        .departmentName(newLecturer.getDepartment().getDepartmentName())
        .build();
  }

  @Override
  @Transactional
  public Optional<LecturerResponseDTO> updateLecturer(int id, UpdateLecturerRequestDTO lecturer) {
    Optional<Lecturer> existingLecturer = lecturerRepository.findByLecturerId(id);
    if (existingLecturer.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }
    Integer departmentId = lecturer.getDepartmentId();
    Optional<Department> department = departmentRepository.findByDepartmentId(departmentId);
    if (department.isEmpty()) {
      throw new RuntimeException("Department with ID " + departmentId + " not found");
    }
    Lecturer updatedLecturer = Lecturer.builder()
        .lecturerId(existingLecturer.get().getLecturerId())
        .firstName(lecturer.getFirstName())
        .lastName(lecturer.getLastName())
        .email(lecturer.getEmail())
        .phoneNumber(lecturer.getPhoneNumber())
        .address(lecturer.getAddress())
        .dateOfBirth(lecturer.getDateOfBirth())
        .department(department.get())
        .build();

    return Optional.of(LecturerResponseDTO.builder()
        .lecturerId(updatedLecturer.getLecturerId())
        .firstName(updatedLecturer.getFirstName())
        .lastName(updatedLecturer.getLastName())
        .email(updatedLecturer.getEmail())
        .phoneNumber(updatedLecturer.getPhoneNumber())
        .address(updatedLecturer.getAddress())
        .dateOfBirth(String.valueOf(updatedLecturer.getDateOfBirth()))
        .departmentName(updatedLecturer.getDepartment().getDepartmentName())
        .build());
  }

  @Override
  @Transactional
  public boolean deleteLecturer(int id) {
    Optional<Lecturer> lecturerOpt = lecturerRepository.findByLecturerId(id);
    if (lecturerOpt.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }

    Lecturer lecturer = lecturerOpt.get();

    if (lecturer.getCourses() != null) {
      for (Course course : lecturer.getCourses()) {
        course.getLecturers().remove(lecturer);
      }
      lecturer.getCourses().clear();
    }

    if (lecturer.getSessions() != null && !lecturer.getSessions().isEmpty()) {
      throw new RuntimeException("Cannot delete lecturer with existing sessions. Please reassign or delete sessions first.");
    }
    lecturerRepository.delete(lecturer);
    return true;
  }

  @Override
  public Optional<LectureCourseResponseDTO> getLecturerDetails(int id) {
    Optional<Lecturer> lecturer = lecturerRepository.findByLecturerId(id);
    if (lecturer.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }

    List<CourseResponseDTO> courseDTOs = courseRepository.findByLecturerId(id)
        .stream()
        .map(course -> CourseResponseDTO.partialBuilder()
            .courseId(course.getCourseId())
            .courseName(course.getCourseName())
            .courseCode(course.getCourseCode())
            .semester(course.getSemester())
            .build())
        .toList();

    LectureCourseResponseDTO response = LectureCourseResponseDTO.builder()
        .lecturerId(lecturer.get().getLecturerId())
        .firstName(lecturer.get().getFirstName())
        .lastName(lecturer.get().getLastName())
        .email(lecturer.get().getEmail())
        .phoneNumber(lecturer.get().getPhoneNumber())
        .address(lecturer.get().getAddress())
        .dateOfBirth(String.valueOf(lecturer.get().getDateOfBirth()))
        .departmentName(lecturer.get().getDepartment().getDepartmentName())
        .courses(courseDTOs)
        .build();

    return Optional.of(response);
  }
}
