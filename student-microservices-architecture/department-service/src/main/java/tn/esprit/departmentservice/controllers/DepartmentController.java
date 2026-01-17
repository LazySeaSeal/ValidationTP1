package tn.esprit.departmentservice.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.departmentservice.entities.Department;
import tn.esprit.departmentservice.services.IDepartmentService;

import java.util.List;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class DepartmentController {
    @Autowired
    private IDepartmentService departmentService;

    @GetMapping("/getAllDepartment")
    public List<Department> getAllDepartment() { return departmentService.getAllDepartments(); }

    @GetMapping("/getDepartment/{id}")
    public Department getDepartment(@PathVariable Long id) {
        Department dep=departmentService.getDepartmentById(id);
        System.out.println(dep);
        return dep;
    }
@PostMapping("/createDepartment")
public Department createDepartment(@RequestBody Department department) { return departmentService.saveDepartment(department); }

@PutMapping("/updateDepartment")
public Department updateDepartment(@RequestBody Department department) {
    return departmentService.saveDepartment(department);
}

@DeleteMapping("/deleteDepartment/{id}")
public void deleteDepartment(@PathVariable Long id) {
    departmentService.deleteDepartment(id); }
}
