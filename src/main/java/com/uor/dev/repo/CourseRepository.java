package com.uor.dev.repo;

import com.uor.dev.entity.Course;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;
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
}
