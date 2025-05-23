package com.uor.dev.payload.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateStudentRequestDTO {

  @NotBlank(message = "First name must be not blank")
  private String firstName;

  @NotBlank(message = "Last name must be not blank")
  private String lastName;

  @Email
  @NotBlank(message = "Email must be not blank")
  private String email;

  @NotBlank(message = "Phone number must be not blank")
  private String phoneNumber;

  @NotBlank(message = "Address must be not blank")
  private String address;

  @NotNull(message = "Date of birth must be not null")
  private LocalDate dateOfBirth;
}
