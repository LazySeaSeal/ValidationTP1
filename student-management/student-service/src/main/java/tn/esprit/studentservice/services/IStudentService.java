package tn.esprit.studentservice.services;

import tn.esprit.studentservice.entities.Student;

import java.util.List;
import java.util.Optional;

public interface IStudentService {
    public List<Student> getAllStudents();
    public Student getStudentById(Long id);
    public Optional<Student> saveStudent(Student student);
    public void deleteStudent(Long id);
}
