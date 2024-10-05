package ua.laboratory.lab_spring_task.serviceTests;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.dao.implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.dao.implementation.TrainerDAOImpl;
import ua.laboratory.lab_spring_task.dao.implementation.TrainingTypeDAOImpl;
import ua.laboratory.lab_spring_task.dao.implementation.UserDAOImpl;
import ua.laboratory.lab_spring_task.dao.TraineeRepository;
import ua.laboratory.lab_spring_task.dao.TrainerRepository;
import ua.laboratory.lab_spring_task.dao.TrainingTypeRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.service.implementation.TraineeServiceImpl;
import ua.laboratory.lab_spring_task.service.implementation.TrainerServiceImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeServiceTests {
    private TraineeServiceImpl traineeService;
    private TrainerServiceImpl trainerService;
    private static SessionFactory sessionFactory;
    private UserRepository userDAO;
    private User presavedUser;
    private TraineeRepository traineeDAO;
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
        TrainingTypeRepository trainingTypeDAO = new TrainingTypeDAOImpl(sessionFactory);
        traineeDAO = new TraineeDAOImpl(userDAO,sessionFactory);
        TrainerRepository trainerDAO = new TrainerDAOImpl(userDAO, trainingTypeDAO, sessionFactory, traineeDAO);
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
