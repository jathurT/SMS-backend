package com.uor.dev.payload.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponseDTO {
  private Integer studentId;
  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private String address;
  private String dateOfBirth;
}
