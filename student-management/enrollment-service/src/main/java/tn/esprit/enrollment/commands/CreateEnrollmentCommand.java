package tn.esprit.enrollment.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEnrollmentCommand {
    @TargetAggregateIdentifier
    private String enrollmentId;
    private Long studentId;
    private Long courseId;
    private String status;
}
