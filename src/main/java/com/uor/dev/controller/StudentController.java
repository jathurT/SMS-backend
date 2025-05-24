package com.uor.dev.controller;


import com.uor.dev.payload.student.CreateStudentRequestDTO;
import com.uor.dev.payload.student.StudentResponseDTO;
import com.uor.dev.payload.student.UpdateStudentRequestDTO;
import com.uor.dev.service.StudentService;
import com.uor.dev.util.ResponseEntity;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Optional;

@Path("/api/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentController {

  @Inject
  StudentService studentService;

  @GET
  @Path("/all")
  public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
    List<StudentResponseDTO> students = studentService.getAllStudents();
    return ResponseEntity.ok(students);
  }

  @GET
  @Path("/{id}")
  public ResponseEntity<StudentResponseDTO> getStudentById(@PathParam("id") int id) {
    Optional<StudentResponseDTO> student = studentService.getStudentById(id);
    return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound("Student not found"));
  }

  @POST
  @Path("/add")
  public ResponseEntity<StudentResponseDTO> addStudent(@Valid CreateStudentRequestDTO student) {
    StudentResponseDTO createdStudent = studentService.addStudent(student);
    return ResponseEntity.created(createdStudent);
  }

  @PUT
  @Path("/update/{id}")
  public ResponseEntity<StudentResponseDTO> updateStudent(@PathParam("id") int id, @Valid UpdateStudentRequestDTO student) {
    Optional<StudentResponseDTO> updatedStudent = studentService.updateStudent(id, student);
    return updatedStudent.map(ResponseEntity::ok).orElse(ResponseEntity.notFound("Student not found"));
  }

  @DELETE
  @Path("/delete/{id}")
  public ResponseEntity<Void> deleteStudent(@PathParam("id") int id) {
    boolean deleted = studentService.deleteStudent(id);
    if (deleted) {
      return ResponseEntity.noContent();
    } else {
      return ResponseEntity.notFound("Student not found");
    }
  }

  @GET
  @Path("/email/{email}")
  public ResponseEntity<StudentResponseDTO> getStudentByEmail(@PathParam("email") String email) {
    Optional<StudentResponseDTO> student = studentService.getStudentByEmail(email);
    return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound("Student not found"));
  }
}
