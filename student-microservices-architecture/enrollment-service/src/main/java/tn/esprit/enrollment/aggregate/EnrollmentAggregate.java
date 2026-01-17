package tn.esprit.enrollment.aggregate;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import tn.esprit.enrollment.commands.CreateEnrollmentCommand;
import tn.esprit.enrollment.commands.UpdateEnrollmentStatusCommand;
import tn.esprit.enrollment.events.EnrollmentCreatedEvent;
import tn.esprit.enrollment.events.EnrollmentStatusUpdatedEvent;

@Aggregate
@NoArgsConstructor
public class EnrollmentAggregate {

    @AggregateIdentifier
    private String enrollmentId;
    private Long studentId;
    private Long courseId;
    private String status;

    @CommandHandler
    public EnrollmentAggregate(CreateEnrollmentCommand command) {
        // Validate command
        if (command.getStudentId() == null || command.getCourseId() == null) {
            throw new IllegalArgumentException("Student ID and Course ID are required");
        }

        // Apply event
        AggregateLifecycle.apply(new EnrollmentCreatedEvent(
                command.getEnrollmentId(),
                command.getStudentId(),
                command.getCourseId(),
                command.getStatus()));
    }

    @CommandHandler
    public void handle(UpdateEnrollmentStatusCommand command) {
        // Validate command
        if (command.getStatus() == null || command.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }

        // Apply event
        AggregateLifecycle.apply(new EnrollmentStatusUpdatedEvent(
                command.getEnrollmentId(),
                command.getStatus()));
    }

    @EventSourcingHandler
    public void on(EnrollmentCreatedEvent event) {
        this.enrollmentId = event.getEnrollmentId();
        this.studentId = event.getStudentId();
        this.courseId = event.getCourseId();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(EnrollmentStatusUpdatedEvent event) {
        this.status = event.getStatus();
    }
}
