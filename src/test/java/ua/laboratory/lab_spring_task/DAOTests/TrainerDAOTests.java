package ua.laboratory.lab_spring_task.DAOTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainerDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.Model.Enum.TrainingType;
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

    }

    @Test
    public void testCreateTrainer() {
//        Trainer trainer = new Trainer("Tom", "Thompson",
//                true, 3L,  TrainingType.Agility);
//        trainerDAO.createTrainer(trainer);
//
//        assertEquals(3, storage.size());
//        assertTrue(storage.containsKey(trainer.getId()));
    }

    @Test
    public void testGetTrainer() {
        Trainer found = trainerDAO.getTrainer(1L);

        assertEquals(2, storage.size());
        assertTrue(storage.containsKey(found.getId()));
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> found = trainerDAO.getAllTrainers();

        assertEquals(2, found.size());
        assertTrue(storage.containsKey(found.get(0).getId()));
        assertTrue(storage.containsKey(found.get(1).getId()));
    }

    @Test
    public void testUpdateTrainer() {
//        Trainer updatedTrainer = new Trainer("Jon", "Thompson",
//                true, 2L, TrainingType.Agility);
//        trainerDAO.updateTrainer(updatedTrainer);
//
//        assertEquals(2, storage.size());
//        assertEquals(updatedTrainer.getFirstName(), storage.get(2L).getFirstName());
    }
}
