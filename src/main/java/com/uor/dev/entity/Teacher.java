package com.uor.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "teacher_id")
  private Long teacherId;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;
}