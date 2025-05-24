package com.uor.dev.payload.session;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateSessionRequestDTO {

  @NotNull(message = "Date must be not null")
  private LocalDate date;

  @NotNull(message = "Start time must be not null")
  private LocalTime startTime;

  @NotNull(message = "End time must be not null")
  private LocalTime endTime;
}
