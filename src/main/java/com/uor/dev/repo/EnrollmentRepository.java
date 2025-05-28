package com.uor.dev.repo;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Enrollment;
import com.uor.dev.entity.Student;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
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

  public List<Enrollment> findByStudent(Student student) {
    return find("student", student).list();
  }

  public Collection<Enrollment> findByCourseId(Integer courseId) {
    return find("course.courseId", courseId).list();
  }

  public List<Enrollment> findByCourse(Course course) {
    return find("course", course).list();
  }
}
