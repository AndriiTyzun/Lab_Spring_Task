package ua.laboratory.lab_spring_task.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainerDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Service.Implementation.TrainerServiceImpl;
import ua.laboratory.lab_spring_task.Service.TrainerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerServiceTests {
    private final Map<Long, Trainer> storage;
    private final TrainerDAO trainerDAO;
    private final TrainerService trainerService;

    public TrainerServiceTests() {
        storage = new HashMap<>();
        trainerDAO = new TrainerDAOImpl(storage);
        trainerService = new TrainerServiceImpl(trainerDAO);
    }

    @BeforeEach
    public void setUp() {
        storage.clear();
        storage.put(1L, new Trainer("Tom", "Thompson","tom.tompson","abctgFdJQ5",
                true, 1L, "Heavy weight"));
        storage.put(2L, new Trainer("John", "Thompson","john.tompson","abctgFdJQ5",
                true, 2L, "Athletics"));
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer("Tom", "Thompson","tom.tompson","abctgFdJQ5",
                true, 3L, "Athletics");
        trainerService.createTrainer(trainer);

        assertEquals(3, storage.size());
        assertEquals("Tom.Thompson", trainerService.getTrainer(3L).getUsername());
        assertFalse(trainerService.getTrainer(3L).getPassword().isEmpty());
        assertTrue(storage.containsKey(trainer.getUserId()));
    }

    @Test
    public void testGetTrainer() {
        Trainer found = trainerService.getTrainer(1L);

        assertEquals(2, storage.size());
        assertTrue(storage.containsKey(found.getUserId()));
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> found = trainerService.getAllTrainers();

        assertEquals(2, found.size());
        assertTrue(storage.containsKey(found.get(0).getUserId()));
        assertTrue(storage.containsKey(found.get(1).getUserId()));
    }

    @Test
    public void testUpdateTrainer() {
        Trainer updatedTrainer = new Trainer("Jon", "Thompson","tom.tompson","abctgFdJQ5",
                true, 2L, "Athletics");
        trainerDAO.updateTrainer(updatedTrainer);

        assertEquals(2, storage.size());
        assertEquals(updatedTrainer.getFirstName(), storage.get(2L).getFirstName());
    }
}
