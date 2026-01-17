package tn.esprit.enrollment.query;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enrollment_query")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentQueryModel {

    @Id
    private String enrollmentId;
    private Long studentId;
    private Long courseId;
    private String status;
}
