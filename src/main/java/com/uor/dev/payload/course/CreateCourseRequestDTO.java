package com.uor.dev.payload.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCourseRequestDTO {
  @NotBlank(message = "Course name must be not blank")
  private String courseName;

  @NotBlank(message = "Course code must be not blank")
  private String courseCode;

  @NotNull(message = "Credits must be not null")
  private Integer credits;

  @NotBlank(message = "Semester must be not null")
  private String semester;

  @NotBlank(message = "Department name must be not blank")
  private String departmentName;

  @NotBlank(message = "Enrollment key must be not blank")
  private String enrollmentKey;
}
