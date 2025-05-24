package com.uor.dev.service;

import com.uor.dev.payload.department.CreateDepartmentRequestDTO;
import com.uor.dev.payload.department.DepartmentResponseDTO;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
  List<DepartmentResponseDTO> getAllDepartments();

  Optional<DepartmentResponseDTO> getDepartmentById(int id);

  DepartmentResponseDTO addDepartment(CreateDepartmentRequestDTO department);

  Optional<DepartmentResponseDTO> updateDepartment(int id, CreateDepartmentRequestDTO department);

  boolean deleteDepartment(int id);
}
