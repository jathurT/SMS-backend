package com.uor.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "course_id")
  private Long courseId;

  @Column(name = "course_name", nullable = false)
  private String courseName;

  @Column(name = "course_code", unique = true, nullable = false)
  private String courseCode;

  @Column(name = "credits")
  private Integer credits;

  @Column(name = "semester")
  private String semester;
}