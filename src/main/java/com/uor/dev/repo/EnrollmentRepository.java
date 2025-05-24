package com.uor.dev.repo;

import com.uor.dev.entity.Enrollment;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EnrollmentRepository implements PanacheRepository<Enrollment> {
  public Optional<Enrollment> findByEnrollmentId(int id) {
    return find("enrollmentId", id).firstResultOptional();
  }

  public List<Enrollment> findByStudentId(int studentId) {
    return find("student.studentId", studentId).list();
  }

  public Integer countByCourseId(Integer courseId) {
    return (int) count("course.courseId", courseId);
  }
}
