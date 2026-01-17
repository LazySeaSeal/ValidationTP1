package tn.esprit.enrollment.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentQueryModel, String> {
    List<EnrollmentQueryModel> findByStudentId(Long studentId);

    List<EnrollmentQueryModel> findByCourseId(Long courseId);
}
