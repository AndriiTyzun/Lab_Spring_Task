package ua.laboratory.lab_spring_task.DAOTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Util.InMemoryStorage;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TrainerDAOTests {
    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private InMemoryStorage storage;
    private Map<Long, Trainer> trainerStorage;

    @BeforeEach
    public void setUp() {
        storage.getTrainerStorage().clear();
        trainerStorage = storage.getTrainerStorage();
        trainerStorage.put(1L, new Trainer("Tom", "Thompson", "tom.tompson", "abctgFdJQ5",
                true, 1L, "Heavy weight"));
        trainerStorage.put(2L, new Trainer("John", "Thompson", "john.tompson", "abctgFdJQ5",
                true, 2L, "Athletics"));
    }

    @Test
    public void testCreateTrainer() {
        Trainer trainer = new Trainer("Tom", "Thompson", "tom.tompson", "abctgFdJQ5",
                true, 3L, "Athletics");
        trainerDAO.createTrainer(trainer);

        assertEquals(3, storage.getTrainerStorage().size());
        assertTrue(storage.getTrainerStorage().containsKey(trainer.getUserId()));
    }

    @Test
    public void testGetTrainer() {
        Trainer found = trainerDAO.getTrainer(1L);

        assertEquals(2, storage.getTrainerStorage().size());
        assertTrue(trainerStorage.containsKey(found.getUserId()));
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> found = trainerDAO.getAllTrainers();

        assertEquals(2, found.size());
        assertTrue(trainerStorage.containsKey(found.get(0).getUserId()));
        assertTrue(trainerStorage.containsKey(found.get(1).getUserId()));
    }

    @Test
    public void testUpdateTrainer() {
        Trainer updatedTrainer = new Trainer("Jon", "Thompson", "john.tompson", "abctgFdJQ5",
                true, 2L, "Athletics");
        trainerDAO.updateTrainer(updatedTrainer);

        assertEquals(2, storage.getTrainerStorage().size());
        assertEquals(updatedTrainer.getFirstName(), trainerStorage.get(2L).getFirstName());
    }
}
