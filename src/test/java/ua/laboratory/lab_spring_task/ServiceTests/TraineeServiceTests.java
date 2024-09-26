package ua.laboratory.lab_spring_task.ServiceTests;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.Implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainerDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainingTypeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.UserDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.DAO.TrainingTypeDAO;
import ua.laboratory.lab_spring_task.DAO.UserDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.TrainingType;
import ua.laboratory.lab_spring_task.Model.User;
import ua.laboratory.lab_spring_task.Service.Implementation.TraineeServiceImpl;
import ua.laboratory.lab_spring_task.Service.Implementation.TrainerServiceImpl;
import ua.laboratory.lab_spring_task.Service.TraineeService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeServiceTests {
    private TraineeServiceImpl traineeService;
    private TrainerServiceImpl trainerService;
    private static SessionFactory sessionFactory;
    User presavedUser;
    Trainee presavedTrainee;
    Trainer presavedTrainer;
    TrainingType presavedTrainingType;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        UserDAO userDAO = new UserDAOImpl(sessionFactory);
        TrainingTypeDAO trainingTypeDAO = new TrainingTypeDAOImpl(sessionFactory);
        TraineeDAO traineeDAO = new TraineeDAOImpl(userDAO,sessionFactory);
        TrainerDAO trainerDAO = new TrainerDAOImpl(userDAO, trainingTypeDAO, sessionFactory, traineeDAO);
        traineeService = new TraineeServiceImpl(traineeDAO);
        trainerService = new TrainerServiceImpl(trainerDAO);

        presavedTrainingType = trainingTypeDAO.addTrainingType( new TrainingType("Agility"));
        presavedUser = userDAO.createOrUpdateUser(new User("John", "Doe","j.d","1233211231", true));
        presavedTrainee = traineeService.createOrUpdateTrainee(new Trainee(LocalDate.now(), "City, Street, House 1", presavedUser));
        presavedTrainer = trainerDAO.createOrUpdateTrainer(new Trainer(presavedTrainingType, presavedUser));
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateTrainee() {
        User user = new User("John", "Doe","j.d","1233211231", true);
        Trainee trainee = new Trainee(LocalDate.now(), "City, Street, House 1", user);

        Trainee savedTrainee = traineeService.createOrUpdateTrainee(trainee);

        assertNotNull(savedTrainee);
        assertEquals(trainee.getUser().getUsername(), savedTrainee.getUser().getUsername());
    }

    @Test
    void testCheckCredentials() {
        Trainee trainee = traineeService.createOrUpdateTrainee(presavedTrainee);
        Boolean result = traineeService.checkCredentials(trainee.getUser().getUsername(), trainee.getUser().getPassword());

        assertTrue(result);
    }

    @Test
    void testGetTraineeById() {
        Trainee foundTrainee = traineeService.getTraineeById(1L);

        assertNotNull(foundTrainee);
        assertEquals(presavedTrainee.getId(), foundTrainee.getId());
    }

    @Test
    void testGetTraineeByUsername() {
        Trainee foundTrainee = traineeService.getTraineeByUsername("John.Doe");

        assertNotNull(foundTrainee);
        assertEquals(presavedTrainee.getUser().getUsername(), foundTrainee.getUser().getUsername());
    }

    @Test
    void testChangePassword() {
        Trainee updatedTrainee = traineeService.changePassword("John.Doe", "newPassword");

        assertNotNull(updatedTrainee);
        assertEquals("newPassword", updatedTrainee.getUser().getPassword());
    }

    @Test
    void testActivateTrainee() {
        presavedTrainee.getUser().setActive(false);
        traineeService.createOrUpdateTrainee(presavedTrainee);

        Trainee activatedTrainee = traineeService.activateTrainee(1L);

        assertNotNull(activatedTrainee);
        assertTrue(activatedTrainee.getUser().isActive());
    }

    @Test
    void testDeactivateTrainee() {
        Trainee deactivatedTrainee = traineeService.deactivateTrainee(1L);

        assertNotNull(deactivatedTrainee);
        assertFalse(deactivatedTrainee.getUser().isActive());
    }

    @Test
    void testUpdateTrainers() {
        Set<Trainer> trainers = new HashSet<>();
        trainers.add(presavedTrainer);

        Trainee updatedTrainee = traineeService.updateTrainers(1L, trainers);

        assertNotNull(updatedTrainee);
        assertEquals(trainers.size(), updatedTrainee.getTrainers().size());
    }

    @Test
    void testDeleteTraineeById() {
        traineeService.deleteTrainee(1L);

        assertTrue(traineeService.getAllTrainees().isEmpty());
    }

    @Test
    void testDeleteTraineeByUsername() {
        traineeService.deleteTrainee("John.Doe");

        assertTrue(traineeService.getAllTrainees().isEmpty());
    }

    @Test
    void testGetAllTrainees() {
        List<Trainee> trainees = traineeService.getAllTrainees();

        assertNotNull(trainees);
        assertFalse(trainees.isEmpty());
    }
}
