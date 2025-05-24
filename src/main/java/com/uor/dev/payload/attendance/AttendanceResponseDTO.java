package com.uor.dev.payload.attendance;

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
}
