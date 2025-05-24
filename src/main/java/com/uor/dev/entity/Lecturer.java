package com.uor.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "lecturers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lecturer {

  @Id
  @Column(name = "lecturer_id")
  private Integer lecturerId;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "address")
  private String address;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Session> sessions;

  @ManyToMany(mappedBy = "lecturers")
  private List<Course> courses;
}