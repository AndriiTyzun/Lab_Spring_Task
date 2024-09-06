package ua.laboratory.lab_spring_task.DAOTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Util.InMemoryStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TraineeDAOTests {
    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private InMemoryStorage storage;
    private Map<Long, Trainee> traineeStorage;

    @BeforeEach
    public void setUp() {
        storage.getTraineeStorage().clear();
        traineeStorage = storage.getTraineeStorage();
        traineeStorage.put(1L, new Trainee("Tom", "Thompson", "tom.tompson", "abctgFdJQ5",
                true, 1L, LocalDate.now(), "City, Street, House 1"));
        traineeStorage.put(2L, new Trainee("John", "Thompson", "john.tompson", "abctgFdJQ5",
                true, 2L, LocalDate.now(), "City, Street, House 2"));
    }

    @Test
    public void testCreateTrainee() {
        Trainee trainee = new Trainee("Tom", "Thompson", "tom.tompson", "abctgFdJQ5",
                true, 3L, LocalDate.now(), "City, Street, House 1");
        traineeDAO.createTrainee(trainee);

        assertEquals(3, storage.getTraineeStorage().size());
        assertTrue(storage.getTraineeStorage().containsKey(trainee.getUserId()));
    }

    @Test
    public void testGetTrainee() {
        Trainee found = traineeDAO.getTraineeById(1L);

        assertEquals(2, storage.getTraineeStorage().size());
        assertTrue(traineeStorage.containsKey(found.getUserId()));
    }

    @Test
    public void testGetAllTrainees() {
        List<Trainee> found = traineeDAO.getAllTrainees();

        assertEquals(2, found.size());
        assertTrue(traineeStorage.containsKey(found.get(0).getUserId()));
        assertTrue(traineeStorage.containsKey(found.get(1).getUserId()));
    }

    @Test
    public void testUpdateTrainee() {
        Trainee updatedTrainee = new Trainee("Jon", "Thompson", "john.tompson", "abctgFdJQ5",
                true, 2L, LocalDate.now(), "City, Street, House 2");
        traineeDAO.updateTrainee(updatedTrainee);

        assertEquals(2, storage.getTraineeStorage().size());
        assertEquals(updatedTrainee.getFirstName(), traineeStorage.get(2L).getFirstName());
    }

    @Test
    public void testDeleteTrainee() {
        traineeDAO.deleteTrainee(2L);

        assertEquals(1, storage.getTraineeStorage().size());
        assertFalse(traineeStorage.containsKey(2L));
    }
}
