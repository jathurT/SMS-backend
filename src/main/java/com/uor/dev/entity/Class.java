package com.uor.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "class_id")
  private Long classId;

  @Column(name = "max_capacity")
  private Integer maxCapacity;

  @Column(name = "date")
  private LocalDate date;

  @Column(name = "start_time")
  private LocalTime startTime;

  @Column(name = "end_time")
  private LocalTime endTime;
}