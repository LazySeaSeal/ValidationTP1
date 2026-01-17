package tn.esprit.enrollment.controller;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.enrollment.commands.CreateEnrollmentCommand;
import tn.esprit.enrollment.commands.UpdateEnrollmentStatusCommand;
import tn.esprit.enrollment.query.EnrollmentQueryModel;
import tn.esprit.enrollment.query.FindEnrollmentByIdQuery;
import tn.esprit.enrollment.query.FindEnrollmentsByStudentQuery;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping
    public CompletableFuture<ResponseEntity<String>> createEnrollment(
            @RequestBody CreateEnrollmentRequest request) {
        String enrollmentId = UUID.randomUUID().toString();
        CreateEnrollmentCommand command = new CreateEnrollmentCommand(
                enrollmentId,
                request.getStudentId(),
                request.getCourseId(),
                "PENDING");
        return commandGateway.send(command)
                .thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(enrollmentId));
    }

    @PutMapping("/{enrollmentId}/status")
    public CompletableFuture<ResponseEntity<String>> updateEnrollmentStatus(
            @PathVariable String enrollmentId,
            @RequestBody UpdateStatusRequest request) {
        UpdateEnrollmentStatusCommand command = new UpdateEnrollmentStatusCommand(
                enrollmentId,
                request.getStatus());
        return commandGateway.send(command)
                .thenApply(result -> ResponseEntity.ok("Status updated successfully"));
    }

    @GetMapping("/{enrollmentId}")
    public CompletableFuture<ResponseEntity<EnrollmentQueryModel>> getEnrollmentById(
            @PathVariable String enrollmentId) {
        return queryGateway.query(
                new FindEnrollmentByIdQuery(enrollmentId),
                EnrollmentQueryModel.class).thenApply(result -> ResponseEntity.ok(result));
    }

    @GetMapping("/student/{studentId}")
    public CompletableFuture<ResponseEntity<List<EnrollmentQueryModel>>> getEnrollmentsByStudent(
            @PathVariable Long studentId) {
        return queryGateway.query(
                new FindEnrollmentsByStudentQuery(studentId),
                List.class).thenApply(result -> ResponseEntity.ok(result));
    }
}

class CreateEnrollmentRequest {
    private Long studentId;
    private Long courseId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}

class UpdateStatusRequest {
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
