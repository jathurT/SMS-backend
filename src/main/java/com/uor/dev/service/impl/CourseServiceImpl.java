package com.uor.dev.service.impl;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Department;
import com.uor.dev.entity.Lecturer;
import com.uor.dev.payload.course.CourseAnalyticResponseDTO;
import com.uor.dev.payload.course.CourseResponseDTO;
import com.uor.dev.payload.course.CreateCourseRequestDTO;
import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import com.uor.dev.payload.session.SessionResponseDTO;
import com.uor.dev.payload.student.StudentResponseDTO;
import com.uor.dev.repo.*;
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

  @Inject
  LecturerRepository lecturerRepository;

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
      CourseResponseDTO dto = CourseResponseDTO.builder()
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

  @Override
  @Transactional
  public Optional<CourseResponseDTO> addLecturerToCourse(int courseId, int lecturerId) {
    Optional<Course> course = courseRepository.findByCourseId(courseId);
    if (course.isEmpty()) {
      throw new RuntimeException("Course not found");
    }
    Optional<Lecturer> lecturer = lecturerRepository.findByLecturerId(lecturerId);
    if (lecturer.isEmpty()) {
      throw new RuntimeException("Lecturer not found");
    }

    if (course.get().getLecturers().stream()
        .anyMatch(l -> l.getLecturerId() == lecturerId)) {
      throw new RuntimeException("Lecturer is already assigned to this course");
    }

    course.get().getLecturers().add(lecturer.get());
    courseRepository.persist(course.get());
    return Optional.of(CourseResponseDTO.builder()
        .courseId(course.get().getCourseId())
        .courseName(course.get().getCourseName())
        .courseCode(course.get().getCourseCode())
        .enrollmentKey(course.get().getEnrollmentKey())
        .credits(course.get().getCredits())
        .semester(course.get().getSemester())
        .departmentName(course.get().getDepartment().getDepartmentName())
        .createdAt(String.valueOf(course.get().getCreatedAt()))
        .lecturers(course.get().getLecturers().stream()
            .map(l -> LecturerResponseDTO.partialBuilder()
                .firstName(l.getFirstName())
                .lastName(l.getLastName())
                .email(l.getEmail())
                .build())
            .toList())
        .totalStudentsEnrolled(enrollmentRepository.countByCourseId(course.get().getCourseId()))
        .build());
  }

  @Override
  public Optional<CourseAnalyticResponseDTO> getCourseDetailsById(int id) {
    Optional<Course> course = courseRepository.findByCourseId(id);
    if (course.isEmpty()) {
      throw new RuntimeException("Course not found");
    }

    List<LecturerResponseDTO> lecturers = course.get().getLecturers().stream()
        .map(lecturer -> LecturerResponseDTO.partialBuilder()
            .firstName(lecturer.getFirstName())
            .lastName(lecturer.getLastName())
            .email(lecturer.getEmail())
            .build())
        .toList();

    List<SessionResponseDTO> sessions = course.get().getSessions().stream()
        .map(session -> SessionResponseDTO.partialBuilder()
            .sessionId(session.getSessionId())
            .date(String.valueOf(session.getDate()))
            .startTime(String.valueOf(session.getStartTime()))
            .endTime(String.valueOf(session.getEndTime()))
            .lecturerName(session.getLecturer().getFirstName() + " " + session.getLecturer().getLastName())
            .build())
        .toList();

    List<StudentResponseDTO> enrolledStudents = enrollmentRepository.findByCourseId(course.get().getCourseId()).stream()
        .map(enrollment -> StudentResponseDTO.basicBuilder()
            .firstName(enrollment.getStudent().getFirstName())
            .email(enrollment.getStudent().getEmail())
            .phoneNumber(enrollment.getStudent().getPhoneNumber())
            .build())
        .toList();

    CourseAnalyticResponseDTO analyticDTO = CourseAnalyticResponseDTO.builder()
        .courseId(course.get().getCourseId())
        .courseName(course.get().getCourseName())
        .courseCode(course.get().getCourseCode())
        .enrollmentKey(course.get().getEnrollmentKey())
        .credits(course.get().getCredits())
        .semester(course.get().getSemester())
        .departmentName(course.get().getDepartment().getDepartmentName())
        .createdAt(String.valueOf(course.get().getCreatedAt()))
        .totalStudentsEnrolled(enrollmentRepository.countByCourseId(course.get().getCourseId()))
        .lecturers(lecturers)
        .conductedSessions(sessions)
        .enrolledStudents(enrolledStudents)
        .totalSessionsConducted(course.get().getSessions().size())
        .build();

    return Optional.of(analyticDTO);
  }

  @Override
  public List<CourseAnalyticResponseDTO> getCourseAnalytics() {
    List<Course> courses = courseRepository.listAll();
    if (courses.isEmpty()) {
      throw new RuntimeException("No courses found");
    }
    List<CourseAnalyticResponseDTO> response = new ArrayList<>();
    for (Course course : courses) {
      List<LecturerResponseDTO> lecturers = course.getLecturers().stream()
          .map(lecturer -> LecturerResponseDTO.partialBuilder()
              .firstName(lecturer.getFirstName())
              .lastName(lecturer.getLastName())
              .email(lecturer.getEmail())
              .build())
          .toList();

      List<SessionResponseDTO> sessions = course.getSessions().stream()
          .map(session -> SessionResponseDTO.partialBuilder()
              .sessionId(session.getSessionId())
              .date(String.valueOf(session.getDate()))
              .startTime(String.valueOf(session.getStartTime()))
              .endTime(String.valueOf(session.getEndTime()))
              .lecturerName(session.getLecturer().getFirstName() + " " + session.getLecturer().getLastName())
              .build())
          .toList();

      List<StudentResponseDTO> enrolledStudents = enrollmentRepository.findByCourseId(course.getCourseId()).stream()
          .map(enrollment -> StudentResponseDTO.basicBuilder()
              .firstName(enrollment.getStudent().getFirstName())
              .email(enrollment.getStudent().getEmail())
              .phoneNumber(enrollment.getStudent().getPhoneNumber())
              .build())
          .toList();

      CourseAnalyticResponseDTO analyticDTO = CourseAnalyticResponseDTO.builder()
          .courseId(course.getCourseId())
          .courseName(course.getCourseName())
          .courseCode(course.getCourseCode())
          .enrollmentKey(course.getEnrollmentKey())
          .credits(course.getCredits())
          .semester(course.getSemester())
          .departmentName(course.getDepartment().getDepartmentName())
          .createdAt(String.valueOf(course.getCreatedAt()))
          .totalStudentsEnrolled(enrollmentRepository.countByCourseId(course.getCourseId()))
          .lecturers(lecturers)
          .conductedSessions(sessions)
          .enrolledStudents(enrolledStudents)
          .totalSessionsConducted(course.getSessions().size())
          .build();
      response.add(analyticDTO);
    }
    return response;
  }
}
