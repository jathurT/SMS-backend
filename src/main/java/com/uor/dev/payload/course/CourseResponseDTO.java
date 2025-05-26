package com.uor.dev.payload.course;

import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
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
  public CourseResponseDTO(Integer courseId, String courseName, String courseCode, String semester,
                           Integer credits, String departmentName, String createdAt, String enrollmentKey) {
    this.courseId = courseId;
    this.courseName = courseName;
    this.courseCode = courseCode;
    this.enrollmentKey = enrollmentKey;
    this.semester = semester;
    this.credits = credits;
    this.departmentName = departmentName;
    this.createdAt = createdAt;
  }

  @Builder(builderMethodName = "fullBuilder")
  public CourseResponseDTO(Integer courseId, String courseName, String courseCode, String semester,
                           Integer credits, String departmentName, List<LecturerResponseDTO> lecturers,
                           Integer totalStudentsEnrolled, String createdAt, String enrollmentKey) {
    this.courseId = courseId;
    this.courseName = courseName;
    this.courseCode = courseCode;
    this.enrollmentKey = enrollmentKey;
    this.semester = semester;
    this.credits = credits;
    this.departmentName = departmentName;
    this.createdAt = createdAt;
    this.lecturers = lecturers;
    this.totalStudentsEnrolled = totalStudentsEnrolled;
  }

}
