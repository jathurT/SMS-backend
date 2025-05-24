package com.uor.dev.controller;

import com.uor.dev.payload.department.CreateDepartmentRequestDTO;
import com.uor.dev.payload.department.DepartmentResponseDTO;
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
  public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
    List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
    return ResponseEntity.ok(departments);
  }

  @GET
  @Path("{id}")
  public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathParam("id") int id) {
    Optional<DepartmentResponseDTO> department = departmentService.getDepartmentById(id);
    return department.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound("Department not found"));
  }

  @POST
  @Path("/add")
  public ResponseEntity<DepartmentResponseDTO> addDepartment(@Valid CreateDepartmentRequestDTO department) {
    DepartmentResponseDTO createdDepartment = departmentService.addDepartment(department);
    return ResponseEntity.created(createdDepartment);
  }

  @PUT
  @Path("/update/{id}")
  public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathParam("id") int id, @Valid CreateDepartmentRequestDTO department) {
    Optional<DepartmentResponseDTO> updatedDepartment = departmentService.updateDepartment(id, department);
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
