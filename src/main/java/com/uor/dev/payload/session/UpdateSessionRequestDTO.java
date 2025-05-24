package com.uor.dev.payload.session;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UpdateSessionRequestDTO {
  @NotNull(message = "Date must be not null")
  private LocalDate date;

  @NotNull(message = "Start time must be not null")
  private LocalTime startTime;

  @NotNull(message = "End time must be not null")
  private LocalTime endTime;

  @NotBlank(message = "Course ID must be not blank")
  private String courseId;

  @NotBlank(message = "Lecturer ID must be not blank")
  private String lecturerId;
}
