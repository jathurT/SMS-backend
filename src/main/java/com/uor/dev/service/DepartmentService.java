package com.uor.dev.service;

import com.uor.dev.entity.Department;
import com.uor.dev.payload.department.CreateDepartmentRequestDTO;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
  List<Department> getAllDepartments();

  Optional<Department> getDepartmentById(int id);

  Department addDepartment(CreateDepartmentRequestDTO department);

  Optional<Department> updateDepartment(int id, CreateDepartmentRequestDTO department);

  boolean deleteDepartment(int id);
}
