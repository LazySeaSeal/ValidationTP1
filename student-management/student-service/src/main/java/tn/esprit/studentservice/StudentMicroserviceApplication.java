package tn.esprit.studentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StudentMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentMicroserviceApplication.class, args);
    }

}
