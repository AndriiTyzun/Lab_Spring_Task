package ua.laboratory.lab_spring_task.DAOTests;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import ua.laboratory.lab_spring_task.DAO.Implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TraineeDAOTests {
    private static SessionFactory sessionFactory;
    private TraineeDAOImpl traineeDAO;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        traineeDAO = new TraineeDAOImpl(sessionFactory);
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateOrUpdateTrainee() {
        Trainee trainee = new Trainee("John", "Doe","john.doe","abctgFdJQ5",
                true,LocalDate.now(), "City, Street, House 1");


        Trainee savedTrainee = traineeDAO.createOrUpdateTrainee(trainee);

        assertNotNull(savedTrainee);
        assertNotNull(savedTrainee.getId());
        assertEquals("John", savedTrainee.getFirstName());
        assertEquals("Doe", savedTrainee.getLastName());
    }

    @Test
    public void testCreateOrUpdateTrainee_UpdateExisting() {
        Trainee trainee = new Trainee("John", "Doe","john.doe","abctgFdJQ5",
                true,LocalDate.now(), "City, Street, House 1");

        Trainee savedTrainee = traineeDAO.createOrUpdateTrainee(trainee);
        assertNotNull(savedTrainee.getId());

        savedTrainee.setLastName("Smith");
        Trainee updatedTrainee = traineeDAO.createOrUpdateTrainee(savedTrainee);

        assertNotNull(updatedTrainee);
        assertEquals(savedTrainee.getId(), updatedTrainee.getId());
        assertEquals("Smith", updatedTrainee.getLastName());
    }

//    @Test
//    public void testFindById() {
//        Trainee trainee = new Trainee();
//        trainee.setFirstName("John");
//        trainee.setLastName("Doe");
//        Trainee savedTrainee = traineeDAO.save(trainee);
//
//        Optional<Trainee> foundTrainee = traineeDAO.findById(savedTrainee.getId());
//
//        assertTrue(foundTrainee.isPresent());
//        assertEquals("John", foundTrainee.get().getFirstName());
//    }
}
