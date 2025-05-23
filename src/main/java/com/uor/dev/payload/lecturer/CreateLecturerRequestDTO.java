package com.uor.dev.payload.lecturer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateLecturerRequestDTO {

  @NotBlank(message = "First name must be not blank")
  private String firstName;

  @NotBlank(message = "Last name must be not blank")
  private String lastName;

  @NotBlank(message = "Email must be not blank")
  @Email
  private String email;

  @NotBlank(message = "Phone number must be not blank")
  private String phoneNumber;

  @NotBlank(message = "Address must be not blank")
  private String address;

  @NotNull(message = "Date of birth must be not null")
  private LocalDate dateOfBirth;

  @NotNull(message = "Department ID must be not null")
  private Integer departmentId;
}
