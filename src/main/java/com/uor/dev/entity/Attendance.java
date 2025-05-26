package com.uor.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendance")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(AttendanceId.class)
public class Attendance {

  @Id
  @Column(name = "session_id")
  private Integer sessionId;

  @Id
  @Column(name = "student_id")
  private Integer studentId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "session_id", insertable = false, updatable = false)
  private Session session;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id", insertable = false, updatable = false)
  private Student student;
}
