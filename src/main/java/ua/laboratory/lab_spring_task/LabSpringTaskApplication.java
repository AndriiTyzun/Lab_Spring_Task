package ua.laboratory.lab_spring_task;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.laboratory.lab_spring_task.Config.AppConfig;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Service.TraineeService;
import ua.laboratory.lab_spring_task.Service.TrainerService;
import ua.laboratory.lab_spring_task.Service.TrainingService;

import java.time.LocalDate;

public class LabSpringTaskApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);
        TrainingService trainingService = context.getBean(TrainingService.class);

        
    }
}
