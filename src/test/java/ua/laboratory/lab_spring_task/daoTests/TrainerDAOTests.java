package ua.laboratory.lab_spring_task.daoTests;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.dao.implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.dao.implementation.TrainerDAOImpl;
import ua.laboratory.lab_spring_task.dao.implementation.TrainingTypeDAOImpl;
import ua.laboratory.lab_spring_task.dao.implementation.UserDAOImpl;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TrainerDAOTests {
    private static SessionFactory sessionFactory;
    private TrainerDAOImpl trainerDAO;
    private TraineeDAOImpl traineeDAO;
    private UserDAOImpl userDAO;
    private TrainingTypeDAOImpl trainingTypeDAO;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);
        trainingTypeDAO = new TrainingTypeDAOImpl(sessionFactory);
        traineeDAO = new TraineeDAOImpl(userDAO, sessionFactory);
        trainerDAO = new TrainerDAOImpl(userDAO,trainingTypeDAO,sessionFactory, traineeDAO);
        User userJD = new User("John", "Doe","j.d","1233211231", true);
        User userJS = new User("John", "Smith","j.s","1233211231", true);
        User userJP = new User("John", "Paul","j.p","1233211231", true);
        Trainer trainerAg = new Trainer(new TrainingType("Agility"), userJD);
        Trainer trainerSt = new Trainer(new TrainingType("Strength"), userJS);
        Trainee trainee = new Trainee(LocalDate.now(), "Street 1", userJP);

        trainee.addTrainer(trainerAg);
        trainerAg.addTrainee(trainee);

        traineeDAO.createOrUpdateTrainee(trainee);
        trainingTypeDAO.addTrainingType(new TrainingType("Agility"));
        trainingTypeDAO.addTrainingType(new TrainingType("Strength"));
        trainerDAO.createOrUpdateTrainer(trainerAg);
        trainerDAO.createOrUpdateTrainer(trainerSt);
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateOrUpdateTrainer() {
        Trainer trainer = new Trainer(new TrainingType("Agility"),
                new User("John", "Doe","john.doe","1233211231", true));

        Trainer savedTrainer = trainerDAO.createOrUpdateTrainer(trainer);

        assertNotNull(savedTrainer);
        assertNotNull(savedTrainer.getId());
        assertEquals("John", savedTrainer.getUser().getFirstName());
        assertEquals("Doe", savedTrainer.getUser().getLastName());
    }

    @Test
    public void testCreateOrUpdateTrainer_UpdateExisting() {
        Trainer trainer = new Trainer(new TrainingType("Agility"),
                new User("John", "Doe","john.doe","1233211231", true));

        Trainer savedTrainer = trainerDAO.createOrUpdateTrainer(trainer);
        assertNotNull(savedTrainer.getId());

        savedTrainer.getUser().setLastName("Smith");
        Trainer updatedTrainer = trainerDAO.createOrUpdateTrainer(savedTrainer);

        assertNotNull(updatedTrainer);
        assertEquals(savedTrainer.getId(), updatedTrainer.getId());
        assertEquals("Smith", updatedTrainer.getUser().getLastName());
    }

    @Test
    public void testFindById() {
        Trainer trainer = trainerDAO.getTrainerById(1L);

        assertNotNull(trainer);
        assertEquals("John", trainer.getUser().getFirstName());
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> trainers = trainerDAO.getAllTrainers();

        assertFalse(trainers.isEmpty());
        assertTrue(trainers.stream().anyMatch(t -> t.getId().equals(1L)));
    }

    @Test
    public void testGetAllTrainersNotAssignedToATrainee() {
        List<Trainer> trainers = trainerDAO.getUnassignedTrainersByTraineeUsername("j.p");

        assertFalse(trainers.isEmpty());
        assertTrue(trainers.stream().anyMatch(t -> t.getId().equals(2L)));
    }
}
