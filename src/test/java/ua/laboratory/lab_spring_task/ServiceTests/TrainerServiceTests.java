package ua.laboratory.lab_spring_task.ServiceTests;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TrainerServiceTests {
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
    public void testCreateTrainer() {
        User user = new User("John", "Doe","j.d","1233211231", true);
        Trainer trainer = new Trainer(presavedTrainingType, user);

        Trainer savedTrainer = trainerService.createOrUpdateTrainer(trainer);

        assertNotNull(savedTrainer);
        assertEquals(trainer.getUser().getUsername(), savedTrainer.getUser().getUsername());
    }

    @Test
    void testCheckCredentials() {
        Trainer trainer = trainerService.createOrUpdateTrainer(presavedTrainer);
        Boolean result = trainerService.checkCredentials(trainer.getUser().getUsername(),
                trainer.getUser().getPassword());

        assertTrue(result);
    }

    @Test
    public void testGetTrainerById() {
        Trainer foundTrainer = trainerService.getTrainerById(1L);

        assertNotNull(foundTrainer);
        assertEquals(presavedTrainee.getId(), foundTrainer.getId());
    }

    @Test
    void testGetTrainerByUsername() {
        Trainer foundTrainer = trainerService.getTrainerByUsername("John.Doe");

        assertNotNull(foundTrainer);
        assertEquals(presavedTrainer.getUser().getUsername(), foundTrainer.getUser().getUsername());
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> trainers = trainerService.getAllTrainers();

        assertNotNull(trainers);
        assertFalse(trainers.isEmpty());
    }

    @Test
    void testChangePassword() {
        Trainer updatedTrainer = trainerService.changePassword("John.Doe", "newPassword");

        assertNotNull(updatedTrainer);
        assertEquals("newPassword", updatedTrainer.getUser().getPassword());
    }

    @Test
    void testActivateTrainer() {
        presavedTrainer.getUser().setActive(false);
        trainerService.createOrUpdateTrainer(presavedTrainer);

        Trainer activatedTrainer = trainerService.activateTrainer(1L);

        assertNotNull(activatedTrainer);
        assertTrue(activatedTrainer.getUser().isActive());
    }

    @Test
    void testDeactivateTrainer() {
        Trainer deactivatedTrainer = trainerService.deactivateTrainer(1L);

        assertNotNull(deactivatedTrainer);
        assertFalse(deactivatedTrainer.getUser().isActive());
    }
}
