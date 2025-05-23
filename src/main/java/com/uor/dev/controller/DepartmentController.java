package com.uor.dev.controller;

import com.uor.dev.entity.Department;
import com.uor.dev.payload.department.CreateDepartmentRequestDTO;
import com.uor.dev.service.DepartmentService;
import com.uor.dev.util.ResponseEntity;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

@Path("/api/departments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentController {

  @Inject
  DepartmentService departmentService;

  @GET
  @Path("/all")
  public ResponseEntity<List<Department>> getAllDepartments() {
    List<Department> departments = departmentService.getAllDepartments();
    return ResponseEntity.ok(departments);
  }

  @GET
  @Path("{id}")
  public ResponseEntity<Department> getDepartmentById(@PathParam("id") int id) {
    Optional<Department> department = departmentService.getDepartmentById(id);
    return department.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Department not found"));
  }

  @POST
  @Path("/add")
  public ResponseEntity<Department> addDepartment(@Valid CreateDepartmentRequestDTO department) {
    Department createdDepartment = departmentService.addDepartment(department);
    return ResponseEntity.created(createdDepartment);
  }

  @PUT
  @Path("/update/{id}")
  public ResponseEntity<Department> updateDepartment(@PathParam("id") int id, @Valid CreateDepartmentRequestDTO department) {
    Optional<Department> updatedDepartment = departmentService.updateDepartment(id, department);
    return updatedDepartment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Department not found"));
  }

  @DELETE
  @Path("/delete/{id}")
  public ResponseEntity<Void> deleteDepartment(@PathParam("id") int id) {
    boolean deleted = departmentService.deleteDepartment(id);
    if (deleted) {
      return ResponseEntity.noContent();
    } else {
      return ResponseEntity.notFound("Department not found");
    }
  }
}
