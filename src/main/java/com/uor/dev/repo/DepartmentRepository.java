package com.uor.dev.repo;

import com.uor.dev.entity.Department;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<Department> {

  public Optional<Department> findByDepartmentId(Integer departmentId) {
    return find("departmentId", departmentId).firstResultOptional();
  }

  public Optional<Department> findByDepartmentName(@NotBlank(message = "Department name must be not blank") String departmentName) {
    return find("departmentName", departmentName).firstResultOptional();
  }
}
