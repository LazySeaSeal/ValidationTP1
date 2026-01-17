package tn.esprit.enrollment.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEnrollmentStatusCommand {
    @TargetAggregateIdentifier
    private String enrollmentId;
    private String status;
}
