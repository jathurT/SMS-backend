package com.uor.dev.payload.student;

import com.uor.dev.payload.course.CourseResponseDTO;
import com.uor.dev.payload.session.SessionResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentAnalyticResponseDTO {
  private Integer studentId;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String address;
  private String dateOfBirth;

  private List<CourseResponseDTO> courses;
  private Integer totalCoursesEnrolled;

  private List<SessionResponseDTO> sessionsAttended;
  private Integer totalSessionsAttended;
}
