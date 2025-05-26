package com.uor.dev.payload.department;

import com.uor.dev.payload.course.CourseResponseDTO;
import com.uor.dev.payload.lecturer.LecturerResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentAnalyticResponseDTO {
  private Integer departmentId;
  private String departmentName;
  private List<LecturerResponseDTO> lecturers;
  private Integer totalLecturers;
  private Integer totalCourses;
  private List<CourseResponseDTO> courses;
}
