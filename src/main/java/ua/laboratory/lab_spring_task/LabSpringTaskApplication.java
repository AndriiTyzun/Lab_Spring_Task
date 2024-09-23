package ua.laboratory.lab_spring_task;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.laboratory.lab_spring_task.Config.AppConfig;
import ua.laboratory.lab_spring_task.DAO.Implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.User;
import ua.laboratory.lab_spring_task.Service.TraineeService;
import ua.laboratory.lab_spring_task.Service.TrainerService;
import ua.laboratory.lab_spring_task.Service.TrainingService;

import java.time.LocalDate;
import java.util.List;


public class LabSpringTaskApplication {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        TraineeService traineeService = context.getBean(TraineeService.class);
//        TrainerService trainerService = context.getBean(TrainerService.class);
//        TrainingService trainingService = context.getBean(TrainingService.class);

        AppConfig config = new AppConfig();
//
//        TraineeDAO traineeDAO = new TraineeDAOImpl(config.sessionFactory());
//
//        Trainee trainee = new Trainee("John", "Doe","john.doe","abctgFdJQ5",
//                true,LocalDate.now(), "City, Street, House 1");
//
//        Trainee savedTrainee = traineeDAO.createOrUpdateTrainee(trainee);
//        Trainee anotherTrainee = traineeDAO.getTraineeById(savedTrainee.getId());

//        try {
//            Configuration configuration = new Configuration().configure();
//            sessionFactoryRegistry = new StandardServiceRegistryBuilder().applySettings(
//                    configuration.getProperties()
//            ).build();
//            sessionFactory = configuration.buildSessionFactory(sessionFactoryRegistry);
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//            throw new ExceptionInInitializerError(e);
//        }
//
//        Session session = sessionFactory.openSession();
//        Transaction tx = null;
//        Long id = null;
//
//        try {
//            tx = session.beginTransaction();
//            Trainee trainee = new Trainee("Tom", "Thompson",
//                    "tom.tompson","abctgFdJQ5",
//                    true, 1L, LocalDate.now(), "City, Street, House 1");
//
//            id = (Long) session.save(trainee);
//
//            List trainees = session.createQuery("from Trainee").list();
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
