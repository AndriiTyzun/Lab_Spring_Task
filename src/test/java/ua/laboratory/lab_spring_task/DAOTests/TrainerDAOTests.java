package ua.laboratory.lab_spring_task.DAOTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainerDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.Model.Trainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainerDAOTests {
    private final Map<Long, Trainer> storage;
    private final TrainerDAO trainerDAO;

    public TrainerDAOTests() {
        storage = new HashMap<>();
        trainerDAO = new TrainerDAOImpl(storage);
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
        Trainer trainer = new Trainer("Tom", "Thompson",
                true, 3L, "Athletics");
        trainerDAO.createTrainer(trainer);

        assertEquals(3, storage.size());
        assertTrue(storage.containsKey(trainer.getTrainerId()));
    }

    @Test
    public void testGetTrainer() {
        Trainer found = trainerDAO.getTrainer(1L);

        assertEquals(2, storage.size());
        assertTrue(storage.containsKey(found.getTrainerId()));
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> found = trainerDAO.getAllTrainers();

        assertEquals(2, found.size());
        assertTrue(storage.containsKey(found.get(0).getTrainerId()));
        assertTrue(storage.containsKey(found.get(1).getTrainerId()));
    }

    @Test
    public void testUpdateTrainer() {
        Trainer updatedTrainer = new Trainer("Jon", "Thompson",
                true, 2L, "Athletics");
        trainerDAO.updateTrainer(updatedTrainer);

        assertEquals(2, storage.size());
        assertEquals(updatedTrainer.getFirstName(), storage.get(2L).getFirstName());
    }
}
