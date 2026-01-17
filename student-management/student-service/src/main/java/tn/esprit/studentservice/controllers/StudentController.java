package tn.esprit.studentservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentservice.entities.Student;
import tn.esprit.studentservice.services.IStudentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class StudentController {
IStudentService studentService;
    @GetMapping("/getAllStudents")
    public List<Student> getAllStudents() { return studentService.getAllStudents(); }

    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable Long id) { return studentService.getStudentById(id); }

    @PostMapping("/createStudent")
    public Optional<Student> createStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }
    @PutMapping("/updateStudent")
    public Optional<Student> updateStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudent(@PathVariable Long id) { studentService.deleteStudent(id); }
}
