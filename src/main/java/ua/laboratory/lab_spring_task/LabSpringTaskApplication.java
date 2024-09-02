package ua.laboratory.lab_spring_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.laboratory.lab_spring_task.DAO.Implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;

@SpringBootApplication
public class LabSpringTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabSpringTaskApplication.class, args);
    }

}
