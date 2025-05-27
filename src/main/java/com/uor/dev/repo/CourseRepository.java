package com.uor.dev.repo;

import com.uor.dev.entity.Course;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CourseRepository implements PanacheRepository<Course> {
  public Optional<Course> findByCourseId(int id) {
    return find("courseId", id).firstResultOptional();
  }

  public Optional<Course> findByCourseName(@NotBlank(message = "Course name must be not blank") String courseName) {
    return find("courseName", courseName).firstResultOptional();
  }


  public Optional<Course> findByCourseCode(@NotBlank(message = "Course code must be not blank") String courseCode) {
    return find("courseCode", courseCode).firstResultOptional();
  }

  public Optional<Course> findByEnrollmentKey(@NotBlank(message = "Enrollment key must be not blank") String enrollmentKey) {
    return find("enrollmentKey", enrollmentKey).firstResultOptional();
  }

  public Integer countByDepartmentId(Integer departmentId) {
    if (departmentId == null) {
      return 0;
    }
    return (int) count("department.departmentId", departmentId);
  }

  public Collection<Course> findByDepartmentId(Integer departmentId) {
    if (departmentId == null) {
      return null;
    }
    return find("department.departmentId", departmentId).list();
  }

  public Collection<Course> findByLecturerId(int id) {
    if (id <= 0) {
      return null;
    }
    return find("select c from Course c join c.lecturers l where l.lecturerId = ?1", id).list();
  }


  public List<Course> findCoursesByStudentId(int id) {
    if (id <= 0) {
      return List.of();
    }
    return find("select c from Course c join c.enrollments e where e.student.studentId = ?1", id).list();
  }
}
