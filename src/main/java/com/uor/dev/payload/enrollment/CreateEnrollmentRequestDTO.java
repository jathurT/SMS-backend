package com.uor.dev.payload.enrollment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEnrollmentRequestDTO {
  @NotNull(message = "Student ID must be not null")
  private Integer studentId;

  @NotBlank(message = "Enrollment key must be not blank")
  private String enrollmentKey;
}
