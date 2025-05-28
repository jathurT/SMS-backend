package com.uor.dev.payload.attendance;

import com.uor.dev.entity.Attendance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceResponseDTO {
  private Integer studentId;
  private String  studentName;
  private Integer sessionId;
  private String sessionName;
  private String lecturerName;
  private String date;
  private String startTime;
  private String endTime;

  @Builder(builderMethodName = "basicBuilder")
  public AttendanceResponseDTO(String studentName, String sessionName, String lecturerName, String date, String startTime, String endTime) {
    this.studentName = studentName;
    this.sessionName = sessionName;
    this.lecturerName = lecturerName;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
  }
}
