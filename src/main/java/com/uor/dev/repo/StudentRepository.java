package com.uor.dev.repo;

import com.uor.dev.entity.Student;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class StudentRepository implements PanacheRepository<Student> {

  public Optional<Student> findByStudentId(int id) {
    return find("studentId", id).firstResultOptional();
  }

  public Optional<Student> findByEmail(String email) {
    return find("email", email).firstResultOptional();
  }


  public Integer countByCourseId(Integer courseId) {
    return (int) count("courseId", courseId);
  }
}
