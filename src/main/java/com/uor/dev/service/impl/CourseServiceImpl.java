package com.uor.dev.service.impl;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Department;
import com.uor.dev.payload.course.CourseResponseDTO;
import com.uor.dev.payload.course.CreateCourseRequestDTO;
import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import com.uor.dev.repo.CourseRepository;
import com.uor.dev.repo.DepartmentRepository;
import com.uor.dev.repo.EnrollmentRepository;
import com.uor.dev.repo.StudentRepository;
import com.uor.dev.service.CourseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CourseServiceImpl implements CourseService {

  @Inject
  CourseRepository courseRepository;

  @Inject
  DepartmentRepository departmentRepository;

  @Inject
  StudentRepository studentRepository;

  @Inject
  EnrollmentRepository enrollmentRepository;

  @Override
  public List<CourseResponseDTO> getAllCourses() {
    List<Course> courses = courseRepository.listAll();
    if (courses.isEmpty()) {
      throw new RuntimeException("No courses found");
    }
    List<CourseResponseDTO> response = new ArrayList<>();
    for (Course course : courses) {
      Integer totalStudentsEnrolled = enrollmentRepository.countByCourseId(course.getCourseId());
      if (totalStudentsEnrolled == 0) {
        totalStudentsEnrolled = null;
      }
      List<LecturerResponseDTO> lecturers = course.getLecturers().stream()
              .map(lecturer -> LecturerResponseDTO.partialBuilder()
                      .firstName(lecturer.getFirstName())
                      .lastName(lecturer.getLastName())
                      .email(lecturer.getEmail())
                      .build())
              .toList();
      CourseResponseDTO dto = CourseResponseDTO.fullBuilder()
              .courseId(course.getCourseId())
              .courseName(course.getCourseName())
              .courseCode(course.getCourseCode())
              .enrollmentKey(course.getEnrollmentKey())
              .credits(course.getCredits())
              .semester(course.getSemester())
              .departmentName(course.getDepartment().getDepartmentName())
              .createdAt(String.valueOf(course.getCreatedAt()))
              .totalStudentsEnrolled(totalStudentsEnrolled)
              .lecturers(lecturers)
              .build();
      response.add(dto);
    }
    return response;
  }

  @Override
  public Optional<CourseResponseDTO> getCourseById(int id) {
    Optional<Course> course = courseRepository.findByCourseId(id);
    if (course.isEmpty()) {
      throw new RuntimeException("Course not found");
    }
    CourseResponseDTO dto = CourseResponseDTO.basicBuilder()
            .courseId(course.get().getCourseId())
            .courseName(course.get().getCourseName())
            .courseCode(course.get().getCourseCode())
            .enrollmentKey(course.get().getEnrollmentKey())
            .credits(course.get().getCredits())
            .semester(course.get().getSemester())
            .departmentName(course.get().getDepartment().getDepartmentName())
            .createdAt(String.valueOf(course.get().getCreatedAt()))
            .build();
    return Optional.of(dto);
  }

  @Override
  @Transactional
  public boolean deleteCourse(int id) {
    Optional<Course> course = courseRepository.findByCourseId(id);
    if (course.isEmpty()) {
      throw new RuntimeException("Course not found");
    }
    courseRepository.delete(course.get());
    return true;
  }

  @Override
  @Transactional
  public CourseResponseDTO addCourse(CreateCourseRequestDTO course) {
    Department department = departmentRepository.findByDepartmentName(course.getDepartmentName())
            .orElseThrow(() -> new RuntimeException("Department with name " + course.getDepartmentName() + " not found"));

    if (courseRepository.findByCourseCode(course.getCourseCode()).isPresent()) {
      throw new RuntimeException("Course with this code already exists");
    }
    if (courseRepository.findByCourseName(course.getCourseName()).isPresent()) {
      throw new RuntimeException("Course with this name already exists");
    }

    if (courseRepository.findByEnrollmentKey(course.getEnrollmentKey()).isPresent()) {
      throw new RuntimeException("Course with this enrollment key already exists");
    }

    Course newCourse = Course.builder()
            .courseName(course.getCourseName())
            .courseCode(course.getCourseCode())
            .enrollmentKey(course.getEnrollmentKey())
            .credits(course.getCredits())
            .semester(course.getSemester())
            .department(department)
            .createdAt(LocalDate.now().atStartOfDay())
            .build();
    courseRepository.persist(newCourse);

    return CourseResponseDTO.basicBuilder()
            .courseId(newCourse.getCourseId())
            .courseCode(newCourse.getCourseCode())
            .courseName(newCourse.getCourseName())
            .enrollmentKey(newCourse.getEnrollmentKey())
            .departmentName(newCourse.getDepartment().getDepartmentName())
            .semester(newCourse.getSemester())
            .credits(newCourse.getCredits())
            .createdAt(String.valueOf(newCourse.getCreatedAt()))
            .build();
  }

  @Override
  @Transactional
  public Optional<CourseResponseDTO> updateCourse(int id, CreateCourseRequestDTO course) {
    Optional<Course> existingCourse = courseRepository.findByCourseId(id);
    if (existingCourse.isEmpty()) {
      throw new RuntimeException("Course not found");
    }
    Course courseToUpdate = existingCourse.get();

    if (courseRepository.findByCourseCode(course.getCourseCode()).isPresent() &&
            !courseToUpdate.getCourseCode().equals(course.getCourseCode())) {
      throw new RuntimeException("Course with this code already exists");
    }

    if (courseRepository.findByCourseName(course.getCourseName()).isPresent() &&
            !courseToUpdate.getCourseName().equals(course.getCourseName())) {
      throw new RuntimeException("Course with this name already exists");
    }

    if (courseRepository.findByEnrollmentKey(course.getEnrollmentKey()).isPresent()
            && !courseToUpdate.getEnrollmentKey().equals(course.getEnrollmentKey())) {
      throw new RuntimeException("Course with this enrollment key already exists");
    }

    Department department = departmentRepository.findByDepartmentName(course.getDepartmentName())
            .orElseThrow(() -> new RuntimeException("Department with name " + course.getDepartmentName() + " not found"));

    courseToUpdate.setCourseName(course.getCourseName());
    courseToUpdate.setCourseCode(course.getCourseCode());
    courseToUpdate.setEnrollmentKey(course.getEnrollmentKey());
    courseToUpdate.setCredits(course.getCredits());
    courseToUpdate.setSemester(course.getSemester());
    courseToUpdate.setDepartment(department);
    courseRepository.persist(courseToUpdate);

    return Optional.of(CourseResponseDTO.basicBuilder()
            .courseId(courseToUpdate.getCourseId())
            .courseCode(courseToUpdate.getCourseCode())
            .enrollmentKey(courseToUpdate.getEnrollmentKey())
            .courseName(courseToUpdate.getCourseName())
            .departmentName(courseToUpdate.getDepartment().getDepartmentName())
            .semester(courseToUpdate.getSemester())
            .credits(courseToUpdate.getCredits())
            .createdAt(String.valueOf(courseToUpdate.getCreatedAt()))
            .build());
  }
}
