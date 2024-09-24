package ua.laboratory.lab_spring_task.DAOTests;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.*;
import ua.laboratory.lab_spring_task.DAO.Implementation.*;
import ua.laboratory.lab_spring_task.Model.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrainingDAOTests {
    private static SessionFactory sessionFactory;
    private TrainingDAO trainingDAO;
    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;
    private UserDAO userDAO;
    private TrainingTypeDAO trainingTypeDAO;
    Trainer trainer;
    Trainee trainee;
    TrainingType trainingType;
    User user;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);
        trainingTypeDAO = new TrainingTypeDAOImpl(sessionFactory);
        traineeDAO = new TraineeDAOImpl(userDAO,sessionFactory);
        trainerDAO = new TrainerDAOImpl(userDAO,trainingTypeDAO,sessionFactory);
        trainingDAO = new TrainingDAOImpl(userDAO, trainingTypeDAO, traineeDAO, sessionFactory);

        user = userDAO.createOrUpdateUser(new User("John", "Doe","j.d","1233211231", true));
        trainingType = trainingTypeDAO.addTrainingType(new TrainingType("Agility"));
        trainee = traineeDAO.createOrUpdateTrainee(new Trainee(LocalDate.now(), "City, Street, House 1", user));
        trainer = trainerDAO.createOrUpdateTrainer(new Trainer(new TrainingType("Agility"), user));
        Training presavedTraining = trainingDAO.createOrUpdateTraining(new Training("New training", trainingType,
                LocalDate.now(),2L,trainee,trainer));
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateOrUpdateTrainer() {
        Training training = new Training("New training", trainingType,
                LocalDate.now(),2L,trainee,trainer);
        Training savedTraining = trainingDAO.createOrUpdateTraining(training);

        assertNotNull(savedTraining);
        assertNotNull(savedTraining.getId());
        assertEquals(trainer.getId(), savedTraining.getTrainer().getId());
        assertEquals(trainee.getId(), savedTraining.getTrainee().getId());
    }

    @Test
    public void testFindById() {
        Training training = trainingDAO.getTrainingById(1L);

        assertNotNull(training);
        assertNotNull(training.getTrainee());
        assertNotNull(training.getTrainer());
        assertEquals("New training", training.getTrainingName());
    }

    @Test
    public void testGetAllTrainers() {
        List<Training> trainings = trainingDAO.getAllTrainings();

        assertFalse(trainings.isEmpty());
        assertTrue(trainings.stream().anyMatch(t -> t.getId().equals(1L)));
    }

}
