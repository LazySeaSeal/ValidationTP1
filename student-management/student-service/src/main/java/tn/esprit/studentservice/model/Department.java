package tn.esprit.studentservice.model;
import jakarta.persistence.ElementCollection;

import java.util.List;

public class Department {
    private Long idDepartment;
    private String name;
    private String location;
    private String phone;
    private String head; // chef de département
    @ElementCollection
    private List<Long> students;
}
