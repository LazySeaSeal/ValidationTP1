package tn.esprit.departmentservice.services;

import tn.esprit.departmentservice.entities.Department;

import java.util.List;

public interface IDepartmentService {
    public List<Department> getAllDepartments();
    public Department getDepartmentById(Long idDepartment);
    public Department saveDepartment(Department department);
    public void deleteDepartment(Long idDepartment);
}