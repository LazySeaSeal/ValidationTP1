package tn.esprit.studentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.studentservice.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
