package com.uor.dev.controller;

import com.uor.dev.payload.attendance.AttendanceResponseDTO;
import com.uor.dev.payload.student.StudentResponseDTO;
import com.uor.dev.service.AttendanceService;
import com.uor.dev.util.ResponseEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;

@Path("/api/attendances")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendanceController {

  @Inject
  AttendanceService attendanceService;

  @GET
  @Path("/all")
  public ResponseEntity<List<AttendanceResponseDTO>> getAllAttendances() {
    List<AttendanceResponseDTO> attendances = attendanceService.getAllAttendances();
    return ResponseEntity.ok(attendances);
  }

  @GET
  @Path("/session/{sessionId}")
  public ResponseEntity<List<AttendanceResponseDTO>> getAttendancesBySessionId(@PathParam("sessionId") int sessionId) {
    List<AttendanceResponseDTO> attendances = attendanceService.getAttendancesBySessionId(sessionId);
    return ResponseEntity.ok(attendances);
  }

  @GET
  @Path("/student/{studentId}")
  public ResponseEntity<List<AttendanceResponseDTO>> getAttendancesByStudentId(@PathParam("studentId") int studentId) {
    List<AttendanceResponseDTO> attendances = attendanceService.getAttendancesByStudentId(studentId);
    return ResponseEntity.ok(attendances);
  }

  @POST
  @Path("/add/session/{sessionId}")
  public ResponseEntity<AttendanceResponseDTO> addAttendance(@PathParam("sessionId") int sessionId,
                                                             @RequestBody int studentId) {
    AttendanceResponseDTO createdAttendance = attendanceService.addAttendance(sessionId, studentId);
    return ResponseEntity.created(createdAttendance);
  }

  @DELETE
  @Path("/delete/session/{sessionId}/student/{studentId}")
  public ResponseEntity<Void> deleteAttendance(@PathParam("sessionId") int sessionId,
                                               @PathParam("studentId") int studentId) {
    boolean deleted = attendanceService.deleteAttendance(sessionId, studentId);
    if (deleted) {
      return ResponseEntity.noContent();
    } else {
      return ResponseEntity.notFound("Attendance not found");
    }
  }

  @GET
  @Path("/non-attending-students/session/{sessionId}")
  public ResponseEntity<List<StudentResponseDTO>> getNonAttendingStudents(@PathParam("sessionId") int sessionId) {
    List<StudentResponseDTO> nonAttendingStudents = attendanceService.getNonAttendingStudents(sessionId);
    return ResponseEntity.ok(nonAttendingStudents);
  }
}
