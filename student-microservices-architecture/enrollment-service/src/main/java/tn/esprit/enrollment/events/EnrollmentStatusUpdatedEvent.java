package tn.esprit.enrollment.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentStatusUpdatedEvent {
    private String enrollmentId;
    private String status;
}
