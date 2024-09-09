package ua.laboratory.lab_spring_task.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.Implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Service.Implementation.TraineeServiceImpl;
import ua.laboratory.lab_spring_task.Service.TraineeService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TraineeServiceTests {
    private final Map<Long, Trainee> storage;
    private final TraineeDAO traineeDAO;
    private final TraineeService traineeService;

    public TraineeServiceTests() {
        storage = new HashMap<>();
        traineeDAO = new TraineeDAOImpl(storage);
        traineeService = new TraineeServiceImpl(traineeDAO);
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
        Trainee trainee = new Trainee("Tom", "Thompson","jon.tompson","abctgFdJQ5",
                true, 3L, LocalDate.now(), "City, Street, House 1");
        traineeService.createTrainee(trainee);

        assertEquals(3, storage.size());
        assertEquals("Tom.Thompson", traineeService.getTraineeById(3L).getUsername());
        assertFalse(traineeService.getTraineeById(3L).getPassword().isEmpty());
        assertTrue(storage.containsKey(trainee.getUserId()));
    }

    @Test
    public void testGetTrainee() {
        Trainee found = traineeService.getTraineeById(1L);

        assertEquals(2, storage.size());
        assertTrue(storage.containsKey(found.getUserId()));
    }

    @Test
    public void testGetAllTrainees() {
        List<Trainee> found = traineeService.getAllTrainees();

        assertEquals(2, found.size());
        assertTrue(storage.containsKey(found.get(0).getUserId()));
        assertTrue(storage.containsKey(found.get(1).getUserId()));
    }

    @Test
    public void testUpdateTrainee() {
        Trainee updatedTrainee = new Trainee("Jon", "Thompson","jon.tompson","abctgFdJQ5",
                true, 2L, LocalDate.now(), "City, Street, House 2");
        traineeService.updateTrainee(updatedTrainee);

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
