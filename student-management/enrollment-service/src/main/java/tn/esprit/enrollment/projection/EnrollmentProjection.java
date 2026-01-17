package tn.esprit.enrollment.projection;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import tn.esprit.enrollment.events.EnrollmentCreatedEvent;
import tn.esprit.enrollment.events.EnrollmentStatusUpdatedEvent;
import tn.esprit.enrollment.query.*;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EnrollmentProjection {

    private final EnrollmentRepository repository;

    @EventHandler
    public void on(EnrollmentCreatedEvent event) {
        EnrollmentQueryModel model = new EnrollmentQueryModel(
                event.getEnrollmentId(),
                event.getStudentId(),
                event.getCourseId(),
                event.getStatus());
        repository.save(model);
    }

    @EventHandler
    public void on(EnrollmentStatusUpdatedEvent event) {
        repository.findById(event.getEnrollmentId()).ifPresent(enrollment -> {
            enrollment.setStatus(event.getStatus());
            repository.save(enrollment);
        });
    }

    @QueryHandler
    public EnrollmentQueryModel handle(FindEnrollmentByIdQuery query) {
        return repository.findById(query.getEnrollmentId()).orElse(null);
    }

    @QueryHandler
    public List<EnrollmentQueryModel> handle(FindEnrollmentsByStudentQuery query) {
        return repository.findByStudentId(query.getStudentId());
    }
}
