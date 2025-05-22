package com.uor.dev.entity;

import com.uor.dev.entity.Course;
import com.uor.dev.entity.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {

  @Id
  @Column(name = "enrollment_id")
  private Integer enrollmentId;

  @Column(name = "enrollment_date", nullable = false)
  private LocalDate enrollmentDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;
}