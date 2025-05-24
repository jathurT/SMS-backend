package com.uor.dev.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceId implements Serializable {
  private Integer sessionId;
  private Integer studentId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AttendanceId that = (AttendanceId) o;
    return Objects.equals(sessionId, that.sessionId) &&
            Objects.equals(studentId, that.studentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, studentId);
  }
}