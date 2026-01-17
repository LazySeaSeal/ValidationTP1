package tn.esprit.studentservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.studentservice.clients.DepartmentClient;
import tn.esprit.studentservice.entities.Student;
import tn.esprit.studentservice.model.Department;
import tn.esprit.studentservice.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DepartmentClient departmentClient;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @CircuitBreaker(name = "departmentService", fallbackMethod = "fallbackSaveStudent")
    @Retry(name = "departmentService")
    @RateLimiter(name = "departmentService")
    public Optional<Student> saveStudent(Student student) {
        logger.info("Attempting to save student with department ID: {}", student.getDepartmentId());
        Department department = departmentClient.getDepartmentById(student.getDepartmentId());
        if (department != null) {
            logger.info("Department found: {}", department.getName());
            return Optional.of(studentRepository.save(student));
        } else {
            logger.warn("Department not found for ID: {}", student.getDepartmentId());
            return Optional.empty();
        }
    }

    // Fallback method for circuit breaker
    public Optional<Student> fallbackSaveStudent(Student student, Throwable throwable) {
        logger.error("Fallback method triggered for student save. Department service is unavailable. Error: {}",
                throwable.getMessage());
        // Save student without validating department (or handle as per business logic)
        student.setDepartmentId(null); // Clear invalid department reference
        return Optional.of(studentRepository.save(student));
    }

    @CircuitBreaker(name = "departmentService", fallbackMethod = "fallbackGetDepartment")
    @Retry(name = "departmentService")
    public Department getDepartmentForStudent(Long departmentId) {
        logger.info("Fetching department with ID: {}", departmentId);
        return departmentClient.getDepartmentById(departmentId);
    }

    // Fallback method for getting department
    public Department fallbackGetDepartment(Long departmentId, Throwable throwable) {
        logger.error("Fallback method triggered for getDepartment. Department service is unavailable. Error: {}",
                throwable.getMessage());
        // Return a default department or null
        Department fallbackDept = new Department();
        fallbackDept.setIdDepartment(departmentId);
        fallbackDept.setName("Service Unavailable");
        return fallbackDept;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

}
