package ua.laboratory.lab_spring_task.DAOTests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainerDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainingTypeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.UserDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TrainingTypeDAO;
import ua.laboratory.lab_spring_task.Model.TrainingType;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TrainerDAOTests {
    private static SessionFactory sessionFactory;
    private TrainerDAOImpl trainerDAO;
    private UserDAOImpl userDAO;
    private TrainingTypeDAOImpl trainingTypeDAO;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);
        trainingTypeDAO = new TrainingTypeDAOImpl(sessionFactory);
        trainerDAO = new TrainerDAOImpl(userDAO,trainingTypeDAO,sessionFactory);
        User user = new User("John", "Doe","j.d","1233211231", true);
        Trainer trainer = new Trainer(new TrainingType("Agility"), user);

        trainingTypeDAO.addTrainingType(new TrainingType("Agility"));
        trainerDAO.createOrUpdateTrainer(trainer);
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
}
