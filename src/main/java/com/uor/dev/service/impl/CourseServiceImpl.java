package com.uor.dev.service.impl;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Department;
import com.uor.dev.payload.course.CreateCourseRequestDTO;
import com.uor.dev.repo.CourseRepository;
import com.uor.dev.repo.DepartmentRepository;
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

  @Override
  public List<Course> getAllCourses() {
    List<Course> courses = new ArrayList<>(courseRepository.listAll());
    if (courses.isEmpty()) {
      throw new RuntimeException("No courses found");
    }
    return courses;
  }

  @Override
  public Optional<Course> getCourseById(int id) {
    Optional<Course> course = courseRepository.findByCourseId(id);
    if (course.isEmpty()) {
      throw new RuntimeException("Course not found");
    }
    return course;
  }

  @Override
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
  public Course addCourse(CreateCourseRequestDTO course) {
    Optional<Course> existingCourse = courseRepository.findByCourseName(course.getCourseName());
    if (existingCourse.isPresent()) {
      throw new RuntimeException("Course with name " + course.getCourseName() + " already exists");
    }

    Department department = departmentRepository.findByDepartmentName(course.getDepartmentName())
        .orElseThrow(() -> new RuntimeException("Department with name " + course.getDepartmentName() + " not found"));

    Course newCourse = new Course();
    updateCourseEntity(course, department, newCourse);
    return newCourse;
  }

  @Override
  @Transactional
  public Optional<Course> updateCourse(int id, CreateCourseRequestDTO course) {
    Optional<Course> existingCourse = courseRepository.findByCourseId(id);
    if (existingCourse.isEmpty()) {
      throw new RuntimeException("Course not found");
    }

    Department department = departmentRepository.findByDepartmentName(course.getDepartmentName())
        .orElseThrow(() -> new RuntimeException("Department with name " + course.getDepartmentName() + " not found"));

    Course updatedCourse = existingCourse.get();
    updateCourseEntity(course, department, updatedCourse);
    return Optional.of(updatedCourse);
  }

  private void updateCourseEntity(CreateCourseRequestDTO course, Department department, Course updatedCourse) {
    updatedCourse.setCourseName(course.getCourseName());
    updatedCourse.setCourseCode(course.getCourseCode());
    updatedCourse.setCredits(course.getCredits());
    updatedCourse.setSemester(course.getSemester());
    updatedCourse.setCreatedAt(LocalDate.now());
    updatedCourse.setDepartment(department);
    courseRepository.persist(updatedCourse);
  }
}
