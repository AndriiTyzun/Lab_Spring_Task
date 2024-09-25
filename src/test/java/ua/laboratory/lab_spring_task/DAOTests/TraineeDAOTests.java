package ua.laboratory.lab_spring_task.DAOTests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import ua.laboratory.lab_spring_task.DAO.Implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.UserDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.User;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TraineeDAOTests {
    private static SessionFactory sessionFactory;
    private TraineeDAOImpl traineeDAO;
    private UserDAOImpl userDAO;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);
        traineeDAO = new TraineeDAOImpl(userDAO,sessionFactory);

        User user = new User("John", "Doe","j.d","1233211231", true);
        Trainee trainee = new Trainee(LocalDate.now(), "City, Street, House 1", user);
        traineeDAO.createOrUpdateTrainee(trainee);
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateOrUpdateTrainee() {
        Trainee trainee = new Trainee(LocalDate.now(), "City, Street, House 1",
                new User("John", "Doe","john.doe","1233211231", true));

        Trainee savedTrainee = traineeDAO.createOrUpdateTrainee(trainee);

        assertNotNull(savedTrainee);
        assertNotNull(savedTrainee.getId());
        assertEquals("John", savedTrainee.getUser().getFirstName());
        assertEquals("Doe", savedTrainee.getUser().getLastName());
    }

    @Test
    public void testCreateOrUpdateTrainee_UpdateExisting() {
        Trainee trainee = new Trainee(LocalDate.now(), "City, Street, House 1",
                new User("John", "Doe","john.doe","1233211231", true));

        Trainee savedTrainee = traineeDAO.createOrUpdateTrainee(trainee);
        assertNotNull(savedTrainee.getId());

        savedTrainee.getUser().setLastName("Smith");
        Trainee updatedTrainee = traineeDAO.createOrUpdateTrainee(savedTrainee);

        assertNotNull(updatedTrainee);
        assertEquals(savedTrainee.getId(), updatedTrainee.getId());
        assertEquals("Smith", updatedTrainee.getUser().getLastName());
    }

    @Test
    public void testFindById() {
        Trainee trainee = traineeDAO.getTraineeById(1L);

        assertNotNull(trainee);
        assertEquals("John", trainee.getUser().getFirstName());
    }

    @Test
    public void testFindByUsername() {
        Trainee trainee = userDAO.getUserByUsername("j.d").getTrainee();

        assertNotNull(trainee);
        assertEquals("John", trainee.getUser().getFirstName());
    }

    @Test
    public void testGetAllTrainees() {
        List<Trainee> trainees = traineeDAO.getAllTrainees();

        assertFalse(trainees.isEmpty());
        assertTrue(trainees.stream().anyMatch(t -> t.getId().equals(1L)));
    }

    @Test
    public void testDeleteById() {
        traineeDAO.deleteTrainee(1L);
        Trainee deletedTrainee = traineeDAO.getTraineeById(1L);
        assertNull(deletedTrainee);
    }
}
