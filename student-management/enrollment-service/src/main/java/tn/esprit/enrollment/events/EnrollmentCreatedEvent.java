package tn.esprit.enrollment.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCreatedEvent {
    private String enrollmentId;
    private Long studentId;
    private Long courseId;
    private String status;
}
