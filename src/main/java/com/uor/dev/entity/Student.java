package com.uor.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "students")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "student_id")
  private Long studentId;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "address")
  private String address;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;
}
