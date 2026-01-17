package tn.esprit.studentservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.studentservice.model.Department;

@FeignClient(name = "DEPARTMENT-MICROSERVICE-APP")
public interface DepartmentClient {
    @GetMapping("/departments/getDepartment/{id}")
    Department getDepartmentById(@PathVariable("id") Long id);
}
