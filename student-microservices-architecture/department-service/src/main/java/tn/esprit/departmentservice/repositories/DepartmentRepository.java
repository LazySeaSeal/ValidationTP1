package tn.esprit.departmentservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.departmentservice.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {}