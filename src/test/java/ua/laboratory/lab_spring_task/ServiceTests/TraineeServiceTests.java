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
import ua.laboratory.lab_spring_task.Model.DTO.Credentials;
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
    private UserDAO userDAO;
    private User presavedUser;
    private TraineeDAO traineeDAO;
    private Trainee presavedTrainee;
    private Trainer presavedTrainer;
    private TrainingType presavedTrainingType;
    private Credentials credentials;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);
        TrainingTypeDAO trainingTypeDAO = new TrainingTypeDAOImpl(sessionFactory);
        traineeDAO = new TraineeDAOImpl(userDAO,sessionFactory);
        TrainerDAO trainerDAO = new TrainerDAOImpl(userDAO, trainingTypeDAO, sessionFactory, traineeDAO);
        traineeService = new TraineeServiceImpl(traineeDAO);
        trainerService = new TrainerServiceImpl(trainerDAO);

        presavedTrainingType = trainingTypeDAO.addTrainingType( new TrainingType("Agility"));
        presavedUser = userDAO.createOrUpdateUser(new User("John", "Doe","j.d","1233211231", true));
        presavedTrainee = traineeDAO.createOrUpdateTrainee(new Trainee(LocalDate.now(), "City, Street, House 1", presavedUser));
        presavedTrainer = trainerDAO.createOrUpdateTrainer(new Trainer(presavedTrainingType, presavedUser));
        credentials = new Credentials(presavedTrainee.getUser().getUsername(), presavedTrainee.getUser().getPassword());
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
        Boolean result = traineeService.checkCredentials(new Credentials(trainee.getUser().getUsername(), trainee.getUser().getPassword()));

        assertTrue(result);
    }

    @Test
    void testGetTraineeById() {
        Trainee foundTrainee = traineeService.getTraineeById(1L,credentials);

        assertNotNull(foundTrainee);
        assertEquals(presavedTrainee.getId(), foundTrainee.getId());
    }

    @Test
    void testGetTraineeByUsername() {
        Trainee foundTrainee = traineeService.getTraineeByUsername("j.d",credentials);

        assertNotNull(foundTrainee);
        assertEquals(presavedTrainee.getUser().getUsername(), foundTrainee.getUser().getUsername());
    }

    @Test
    void testChangePassword() {
        Trainee updatedTrainee = traineeService.changePassword("j.d", "newPassword",credentials);

        assertNotNull(updatedTrainee);
        assertEquals("newPassword", updatedTrainee.getUser().getPassword());
    }

    @Test
    void testActivateTrainee() {
        presavedTrainee.getUser().setActive(false);
        traineeDAO.createOrUpdateTrainee(presavedTrainee);

        Trainee activatedTrainee = traineeService.activateTrainee(1L, credentials);

        assertNotNull(activatedTrainee);
        assertTrue(activatedTrainee.getUser().isActive());
    }

    @Test
    void testDeactivateTrainee() {
        Trainee deactivatedTrainee = traineeService.deactivateTrainee(1L, credentials);

        assertNotNull(deactivatedTrainee);
        assertFalse(deactivatedTrainee.getUser().isActive());
    }

    @Test
    void testUpdateTrainers() {
        Set<Trainer> trainers = new HashSet<>();
        trainers.add(presavedTrainer);

        Trainee updatedTrainee = traineeService.updateTrainers(1L, trainers, credentials);

        assertNotNull(updatedTrainee);
        assertEquals(trainers.size(), updatedTrainee.getTrainers().size());
    }

    @Test
    void testDeleteTraineeById() {
        User anotherUser = userDAO.createOrUpdateUser(new User("John", "Smith","j.s","1233211231", true));
        Trainee anotherUserTrainee = traineeDAO.createOrUpdateTrainee(new Trainee(LocalDate.now(), "City, Street, House 1", anotherUser));

        traineeService.deleteTrainee(2L, credentials);

        assertEquals(1, traineeService.getAllTrainees(credentials).size());
    }

    @Test
    void testDeleteTraineeByUsername() {
        User anotherUser = userDAO.createOrUpdateUser(new User("John", "Smith","j.s","1233211231", true));
        Trainee anotherUserTrainee = traineeDAO.createOrUpdateTrainee(new Trainee(LocalDate.now(), "City, Street, House 1", anotherUser));
        traineeService.deleteTrainee("j.s", credentials);

        assertEquals(1, traineeService.getAllTrainees(credentials).size());
    }

    @Test
    void testGetAllTrainees() {
        List<Trainee> trainees = traineeService.getAllTrainees(credentials);

        assertNotNull(trainees);
        assertFalse(trainees.isEmpty());
    }
}
