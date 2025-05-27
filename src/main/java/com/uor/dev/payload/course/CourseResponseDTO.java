package com.uor.dev.payload.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
public class CourseResponseDTO {

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

  @Builder(builderMethodName = "basicBuilder")
  public CourseResponseDTO(Integer courseId, String courseName, String courseCode,
                           String enrollmentKey, String semester, Integer credits,
                           String departmentName, String createdAt) {
    this.courseId = courseId;
    this.courseName = courseName;
    this.courseCode = courseCode;
    this.enrollmentKey = enrollmentKey;
    this.semester = semester;
    this.credits = credits;
    this.departmentName = departmentName;
    this.createdAt = createdAt;
  }

  @Builder(builderMethodName = "partialBuilder")
  public CourseResponseDTO(Integer courseId, String courseName, String courseCode, String semester) {
    this.courseId = courseId;
    this.courseName = courseName;
    this.courseCode = courseCode;
    this.semester = semester;
  }
}