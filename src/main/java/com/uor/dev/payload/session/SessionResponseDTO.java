package com.uor.dev.payload.session;

import com.uor.dev.payload.student.StudentResponseDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SessionResponseDTO {
  private Integer sessionId;
  private String Date;
  private String startTime;
  private String endTime;
  private String courseName;
  private String CourseCode;
  private String lecturerName;
  private List<StudentResponseDTO> studentsAttended;

  @Builder(builderMethodName = "basicBuilder")
  public SessionResponseDTO(Integer sessionId, String date, String startTime, String endTime,
                            String courseName, String courseCode, String lecturerName) {
    this.sessionId = sessionId;
    this.Date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.courseName = courseName;
    this.CourseCode = courseCode;
    this.lecturerName = lecturerName;
  }

  @Builder(builderMethodName = "fullBuilder")
  public SessionResponseDTO(Integer sessionId, String date, String startTime, String endTime,
                            String courseName, String courseCode, String lecturerName,
                            List<StudentResponseDTO> studentsAttended) {
    this.sessionId = sessionId;
    this.Date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.courseName = courseName;
    this.CourseCode = courseCode;
    this.lecturerName = lecturerName;
    this.studentsAttended = studentsAttended;
  }
}
