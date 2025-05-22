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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "enrollment_id")
  private Long enrollmentId;

  @Column(name = "enrollment_date")
  private LocalDate enrollmentDate;
}