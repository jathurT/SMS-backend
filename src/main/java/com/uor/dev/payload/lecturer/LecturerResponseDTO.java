package com.uor.dev.payload.lecturer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturerResponseDTO {
  private Integer lecturerId;
  private String firstName;
  private String lastName;
  private String dateOfBirth;
  private String email;
  private String phoneNumber;
  private String address;
  private String departmentName;

  @Builder(builderMethodName = "partialBuilder")
  public LecturerResponseDTO(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
}
