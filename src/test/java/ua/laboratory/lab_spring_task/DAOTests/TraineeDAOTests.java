package ua.laboratory.lab_spring_task.DAOTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.Implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class TraineeDAOTests {
    private final Map<Long, Trainee> storage;
    private final TraineeDAO traineeDAO;

    public TraineeDAOTests() {
        storage = new HashMap<>();
        traineeDAO = new TraineeDAOImpl(storage);
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.put(1L, new Trainee("Tom", "Thompson","tom.tompson","abctgFdJQ5",
                true, 1L, LocalDate.now(), "City, Street, House 1"));
        storage.put(2L, new Trainee("John", "Thompson","john.tompson","abctgFdJQ5",
                true, 2L, LocalDate.now(), "City, Street, House 2"));
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee("Tom", "Thompson",
                true, 3L, LocalDate.now(), "City, Street, House 1");
        traineeDAO.createTrainee(trainee);

        assertEquals(3, storage.size());
        assertTrue(storage.containsKey(trainee.getTraineeId()));
    }

    @Test
    public void testGetTrainee() {
        Trainee found = traineeDAO.getTraineeById(1L);

        assertEquals(2, storage.size());
        assertTrue(storage.containsKey(found.getTraineeId()));
    }

    @Test
    public void testGetAllTrainees() {
        List<Trainee> found = traineeDAO.getAllTrainees();

        assertEquals(2, found.size());
        assertTrue(storage.containsKey(found.get(0).getTraineeId()));
        assertTrue(storage.containsKey(found.get(1).getTraineeId()));
    }

    @Test
    public void testUpdateTrainee() {
        Trainee updatedTrainee = new Trainee("Jon", "Thompson",
                true, 2L, LocalDate.now(), "City, Street, House 2");
        traineeDAO.updateTrainee(updatedTrainee);

        assertEquals(2, storage.size());
        assertEquals(updatedTrainee.getFirstName(), storage.get(2L).getFirstName());
    }

    @Test
    public void testDeleteTrainee() {
        traineeDAO.deleteTrainee(2L);

        assertEquals(1, storage.size());
        assertFalse(storage.containsKey(2L));
    }
}
