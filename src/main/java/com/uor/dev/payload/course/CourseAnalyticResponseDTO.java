package com.uor.dev.payload.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import com.uor.dev.payload.session.SessionResponseDTO;
import com.uor.dev.payload.student.StudentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class CourseAnalyticResponseDTO {
  private Integer courseId;
  private String courseName;
  private String courseCode;
  private String enrollmentKey;
  private String semester;
  private Integer credits;
  private String departmentName;
  private List<LecturerResponseDTO> lecturers;
  private Integer totalStudentsEnrolled;
  private String createdAt;
  private List<StudentResponseDTO> enrolledStudents;
  private List<SessionResponseDTO> conductedSessions;
  private Integer totalSessionsConducted;

}