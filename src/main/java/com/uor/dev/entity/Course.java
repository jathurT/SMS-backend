package com.uor.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

  @Id
  @Column(name = "course_id")
  private Integer courseId;

  @Column(name = "course_name", nullable = false)
  private String courseName;

  @Column(name = "course_code", nullable = false, unique = true)
  private String courseCode;

  @Column(name = "credits", nullable = false)
  private Integer credits;

  @Column(name = "semester", nullable = false)
  private String semester;

  @Column(name = "created_at", nullable = false)
  private LocalDate createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Enrollment> enrollments;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Session> sessions;

  @ManyToMany
  @JoinTable(
          name = "teaches",
          joinColumns = @JoinColumn(name = "course_id"),
          inverseJoinColumns = @JoinColumn(name = "lecturer_id")
  )
  private List<Lecturer> lecturers;
}