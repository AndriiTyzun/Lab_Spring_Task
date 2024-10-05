package ua.laboratory.lab_spring_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.laboratory.lab_spring_task.config.AppConfig;


@SpringBootApplication
public class LabSpringTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabSpringTaskApplication.class, args);
    }
}
