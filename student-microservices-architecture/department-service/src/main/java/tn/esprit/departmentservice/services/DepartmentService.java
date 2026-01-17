package tn.esprit.departmentservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.departmentservice.entities.Department;
import tn.esprit.departmentservice.repositories.DepartmentRepository;

import java.util.List;

@Service

public class DepartmentService implements IDepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long idDepartment) {
        return departmentRepository.findById(idDepartment).orElse(null);
    }

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public void deleteDepartment(Long idDepartment) {
        departmentRepository.deleteById(idDepartment);
    }
}