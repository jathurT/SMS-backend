package com.uor.dev.payload.enrollment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponseDTO {
  private Integer enrollmentId;
  private Integer studentId;
  private String studentName;
  private Integer courseId;
  private String courseName;
  private String courseCode;
  private String enrollmentDate;
}
