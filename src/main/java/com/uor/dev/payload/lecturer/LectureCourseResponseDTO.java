package com.uor.dev.payload.lecturer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uor.dev.payload.course.CourseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LectureCourseResponseDTO {

  private Integer lecturerId;
  private String firstName;
  private String lastName;
  private String dateOfBirth;
  private String email;
  private String phoneNumber;
  private String address;
  private String departmentName;

  private List<CourseResponseDTO> courses;
}
