package com.uor.dev.controller;


import com.uor.dev.entity.Student;
import com.uor.dev.payload.student.CreateStudentRequestDTO;
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
  public ResponseEntity<List<Student>> getAllStudents() {
    List<Student> students = studentService.getAllStudents();
    return ResponseEntity.ok(students);
  }

  @GET
  @Path("/{id}")
  public ResponseEntity<Student> getStudentById(@PathParam("id") int id) {
    Optional<Student> student = studentService.getStudentById(id);
    return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound("Student not found"));
  }

  @POST
  @Path("/add")
  public ResponseEntity<Student> addStudent(@Valid CreateStudentRequestDTO student) {
    Student createdStudent = studentService.addStudent(student);
    return ResponseEntity.created(createdStudent);
  }

  @PUT
  @Path("/update/{id}")
  public ResponseEntity<Student> updateStudent(@PathParam("id") int id, @Valid UpdateStudentRequestDTO student) {
    Optional<Student> updatedStudent = studentService.updateStudent(id, student);
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
  public ResponseEntity<Student> getStudentByEmail(@PathParam("email") String email) {
    Optional<Student> student = studentService.getStudentByEmail(email);
    return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound("Student not found"));
  }
}
