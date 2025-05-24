package com.uor.dev.controller;

import com.uor.dev.entity.Attendance;
import com.uor.dev.service.AttendanceService;
import com.uor.dev.util.ResponseEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/attendances")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AttendanceController {

  @Inject
  AttendanceService attendanceService;

  @GET
  @Path("/all")
  public ResponseEntity<List<Attendance>> getAllAttendances() {
    List<Attendance> attendances = attendanceService.getAllAttendances();
    return ResponseEntity.ok(attendances);
  }

  @GET
  @Path("/session/{sessionId}")
  public ResponseEntity<List<Attendance>> getAttendancesBySessionId(@PathParam("sessionId") int sessionId) {
    List<Attendance> attendances = attendanceService.getAttendancesBySessionId(sessionId);
    return ResponseEntity.ok(attendances);
  }

  @GET
  @Path("/student/{studentId}")
  public ResponseEntity<List<Attendance>> getAttendancesByStudentId(@PathParam("studentId") int studentId) {
    List<Attendance> attendances = attendanceService.getAttendancesByStudentId(studentId);
    return ResponseEntity.ok(attendances);
  }

  @POST
  @Path("/add/session/{sessionId}/student/{studentId}")
  public ResponseEntity<Attendance> addAttendance(@PathParam("sessionId") int sessionId,
                                                  @PathParam("studentId") int studentId) {
    Attendance createdAttendance = attendanceService.addAttendance(sessionId, studentId);
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
}
