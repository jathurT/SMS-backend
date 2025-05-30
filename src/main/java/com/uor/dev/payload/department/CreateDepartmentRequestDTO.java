package com.uor.dev.payload.department;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDepartmentRequestDTO {

  @NotBlank(message = "Department name must be not blank")
  private String departmentName;
}
