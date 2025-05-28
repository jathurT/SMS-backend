package com.uor.dev.payload.session;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.uor.dev.payload.student.StudentResponseDTO;
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
public class SessionResponseDTO {

  private Integer sessionId;
  private String date;
  private String startTime;
  private String endTime;
  private String courseName;
  private String courseCode;
  private String lecturerName;
  private List<StudentResponseDTO> studentsAttended;

  @Builder(builderMethodName = "basicBuilder")
  public SessionResponseDTO(Integer sessionId, String date, String startTime, String endTime,
                            String courseName, String courseCode, String lecturerName) {
    this.sessionId = sessionId;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.courseName = courseName;
    this.courseCode = courseCode;
    this.lecturerName = lecturerName;
  }

  @Builder(builderMethodName = "partialBuilder")
  public SessionResponseDTO(Integer sessionId, String date, String startTime, String endTime,
                            String lecturerName) {
    this.sessionId = sessionId;
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.lecturerName = lecturerName;
  }
}
