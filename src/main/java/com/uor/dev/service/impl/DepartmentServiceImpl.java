package com.uor.dev.service.impl;

import com.uor.dev.entity.Department;
import com.uor.dev.payload.department.CreateDepartmentRequestDTO;
import com.uor.dev.repo.DepartmentRepository;
import com.uor.dev.service.DepartmentService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DepartmentServiceImpl implements DepartmentService {

  @Inject
  DepartmentRepository departmentRepository;

  @Override
  public List<Department> getAllDepartments() {
    List<Department> departments = new ArrayList<>(departmentRepository.listAll());
    if (departments.isEmpty()) {
      throw new RuntimeException("No departments found");
    }
    return departments;
  }

  @Override
  public Optional<Department> getDepartmentById(int id) {
    Optional<Department> department = departmentRepository.findByDepartmentId(id);
    if (department.isEmpty()) {
      throw new RuntimeException("Department not found");
    }
    return department;
  }

  @Override
  @Transactional
  public Department addDepartment(CreateDepartmentRequestDTO department) {
    Optional<Department> existingDepartment = departmentRepository.findByDepartmentName(department.getDepartmentName());
    if (existingDepartment.isPresent()) {
      throw new RuntimeException("Department with name " + department.getDepartmentName() + " already exists");
    }

    Department newDepartment = new Department();
    newDepartment.setDepartmentName(department.getDepartmentName());
    departmentRepository.persist(newDepartment);
    return newDepartment;
  }

  @Override
  @Transactional
  public Optional<Department> updateDepartment(int id, CreateDepartmentRequestDTO department) {
    Optional<Department> existingDepartment = departmentRepository.findByDepartmentId(id);
    if (existingDepartment.isEmpty()) {
      throw new RuntimeException("Department not found");
    }

    Department updatedDepartment = existingDepartment.get();
    updatedDepartment.setDepartmentName(department.getDepartmentName());
    departmentRepository.persist(updatedDepartment);
    return Optional.of(updatedDepartment);
  }

  @Override
  public boolean deleteDepartment(int id) {
    Optional<Department> existingDepartment = departmentRepository.findByDepartmentId(id);
    if (existingDepartment.isEmpty()) {
      throw new RuntimeException("Department not found");
    }
    Department departmentToDelete = existingDepartment.get();
    departmentRepository.delete(departmentToDelete);
    return true;
  }
}
