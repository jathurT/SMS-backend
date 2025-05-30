package com.uor.dev.payload.lecturer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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

  @Builder(builderMethodName = "basicBuilder")
  public LecturerResponseDTO(Integer lecturerId, String firstName, String lastName, String email,
                             String phoneNumber) {
    this.lecturerId = lecturerId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }
}
