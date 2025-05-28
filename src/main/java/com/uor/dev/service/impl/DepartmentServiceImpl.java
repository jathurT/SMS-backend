package com.uor.dev.service.impl;

import com.uor.dev.entity.Department;
import com.uor.dev.payload.course.CourseResponseDTO;
import com.uor.dev.payload.department.CreateDepartmentRequestDTO;
import com.uor.dev.payload.department.DepartmentAnalyticResponseDTO;
import com.uor.dev.payload.department.DepartmentResponseDTO;
import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import com.uor.dev.repo.CourseRepository;
import com.uor.dev.repo.DepartmentRepository;
import com.uor.dev.repo.LecturerRepository;
import com.uor.dev.service.DepartmentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {

  @Inject
  DepartmentRepository departmentRepository;

  @Inject
  CourseRepository courseRepository;

  @Inject
  LecturerRepository lecturerRepository;

  @Override
  public List<DepartmentResponseDTO> getAllDepartments() {
    List<Department> departments = departmentRepository.listAll();
    if (departments.isEmpty()) {
      throw new RuntimeException("No departments found");
    }
    List<DepartmentResponseDTO> response = new ArrayList<>();
    for (Department department : departments) {
      DepartmentResponseDTO dto = new DepartmentResponseDTO();
      dto.setDepartmentId(department.getDepartmentId());
      dto.setDepartmentName(department.getDepartmentName());
      response.add(dto);
    }
    return response;
  }

  @Override
  public Optional<DepartmentResponseDTO> getDepartmentById(int id) {
    Optional<Department> department = departmentRepository.findByDepartmentId(id);
    if (department.isEmpty()) {
      throw new RuntimeException("Department not found");
    }
    DepartmentResponseDTO response = new DepartmentResponseDTO();
    response.setDepartmentId(department.get().getDepartmentId());
    response.setDepartmentName(department.get().getDepartmentName());
    return Optional.of(response);
  }

  @Override
  @Transactional
  public DepartmentResponseDTO addDepartment(CreateDepartmentRequestDTO department) {
    if (departmentRepository.findByDepartmentName(department.getDepartmentName()).isPresent()) {
      throw new RuntimeException("Department with this name already exists");
    }
    Department newDepartment = new Department();
    newDepartment.setDepartmentName(department.getDepartmentName());
    departmentRepository.persist(newDepartment);

    DepartmentResponseDTO response = new DepartmentResponseDTO();
    response.setDepartmentId(newDepartment.getDepartmentId());
    response.setDepartmentName(newDepartment.getDepartmentName());
    return response;
  }

  @Override
  @Transactional
  public Optional<DepartmentResponseDTO> updateDepartment(int id, CreateDepartmentRequestDTO department) {
    Optional<Department> existingDepartment = departmentRepository.findByDepartmentId(id);
    if (existingDepartment.isEmpty()) {
      throw new RuntimeException("Department not found");
    }
    Department departmentToUpdate = existingDepartment.get();

    if (departmentRepository.findByDepartmentName(department.getDepartmentName()).isPresent() &&
        !departmentToUpdate.getDepartmentName().equals(department.getDepartmentName())) {
      throw new RuntimeException("Department with this name already exists");
    }

    departmentToUpdate.setDepartmentName(department.getDepartmentName());
    departmentRepository.persist(departmentToUpdate);

    DepartmentResponseDTO response = new DepartmentResponseDTO();
    response.setDepartmentId(departmentToUpdate.getDepartmentId());
    response.setDepartmentName(departmentToUpdate.getDepartmentName());
    return Optional.of(response);
  }

  @Override
  @Transactional
  public boolean deleteDepartment(int id) {
    Optional<Department> existingDepartment = departmentRepository.findByDepartmentId(id);
    if (existingDepartment.isEmpty()) {
      throw new RuntimeException("Department not found");
    }
    Department departmentToDelete = existingDepartment.get();
    departmentRepository.delete(departmentToDelete);
    return true;
  }

  @Override
  public List<DepartmentAnalyticResponseDTO> getDepartmentAnalytics() {
    List<Department> departments = departmentRepository.listAll();
    if (departments.isEmpty()) {
      throw new RuntimeException("No departments found for analytics");
    }
    List<DepartmentAnalyticResponseDTO> response = new ArrayList<>();
    for (Department department : departments) {
      List<LecturerResponseDTO> lecturers = lecturerRepository.findByDepartmentId(department.getDepartmentId())
          .stream()
          .map(lecturer -> LecturerResponseDTO.partialBuilder()
              .firstName(lecturer.getFirstName())
              .lastName(lecturer.getLastName())
              .email(lecturer.getEmail())
              .build())
          .toList();


      List<CourseResponseDTO> courses = courseRepository.findByDepartmentId(department.getDepartmentId())
          .stream()
          .map(course -> CourseResponseDTO.partialBuilder()
              .courseId(course.getCourseId())
              .courseName(course.getCourseName())
              .courseCode(course.getCourseCode())
              .semester(course.getSemester())
              .build())
          .toList();

      DepartmentAnalyticResponseDTO analyticDTO = DepartmentAnalyticResponseDTO.builder()
          .departmentId(department.getDepartmentId())
          .departmentName(department.getDepartmentName())
          .totalCourses(courseRepository.countByDepartmentId(department.getDepartmentId()))
          .totalLecturers(lecturerRepository.countByDepartmentId(department.getDepartmentId()))
          .lecturers(lecturers)
          .courses(courses)
          .build();
      response.add(analyticDTO);
    }
    return response;
  }

  @Override
  public Optional<DepartmentAnalyticResponseDTO> getDepartmentAnalyticsById(int id) {
    Optional<Department> department = departmentRepository.findByDepartmentId(id);
    if (department.isEmpty()) {
      throw new RuntimeException("Department not found");
    }

    List<LecturerResponseDTO> lecturers = lecturerRepository.findByDepartmentId(department.get().getDepartmentId())
        .stream()
        .map(lecturer -> LecturerResponseDTO.partialBuilder()
            .firstName(lecturer.getFirstName())
            .lastName(lecturer.getLastName())
            .email(lecturer.getEmail())
            .build())
        .toList();

    List<CourseResponseDTO> courses = courseRepository.findByDepartmentId(department.get().getDepartmentId())
        .stream()
        .map(course -> CourseResponseDTO.partialBuilder()
            .courseId(course.getCourseId())
            .courseName(course.getCourseName())
            .courseCode(course.getCourseCode())
            .semester(course.getSemester())
            .build())
        .toList();

    DepartmentAnalyticResponseDTO analyticDTO = DepartmentAnalyticResponseDTO.builder()
        .departmentId(department.get().getDepartmentId())
        .departmentName(department.get().getDepartmentName())
        .totalCourses(courseRepository.countByDepartmentId(department.get().getDepartmentId()))
        .totalLecturers(lecturerRepository.countByDepartmentId(department.get().getDepartmentId()))
        .lecturers(lecturers)
        .courses(courses)
        .build();

    return Optional.of(analyticDTO);
  }
}
