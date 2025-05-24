package com.uor.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Enrollment {

  @Id
  @Column(name = "enrollment_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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