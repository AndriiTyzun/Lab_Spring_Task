package ua.laboratory.lab_spring_task.DAOTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.TrainingType;
import ua.laboratory.lab_spring_task.Util.InMemoryStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TrainingDAOTests {
    @Autowired
    private TrainingDAO trainingDAO;

    @Autowired
    private TraineeDAO traineeDAO;

    @Autowired
    private TrainerDAO trainerDAO;

    @Autowired
    private InMemoryStorage storage;

    private Map<Long, Training> trainingStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Trainee> traineeStorage;

    @BeforeEach
    public void setUp() {
        storage.getTrainingStorage().clear();
        storage.getTraineeStorage().clear();
        storage.getTrainerStorage().clear();
        trainingStorage = storage.getTrainingStorage();
        traineeStorage = storage.getTraineeStorage();
        trainerStorage = storage.getTrainerStorage();

        traineeStorage.put(1L, new Trainee("Tom", "Thompson", "tom.tompson", "abctgFdJQ5",
                true, 1L, LocalDate.now(), "City, Street, House 1"));
        traineeStorage.put(2L, new Trainee("John", "Thompson", "john.tompson", "abctgFdJQ5",
                true, 2L, LocalDate.now(), "City, Street, House 2"));

        trainerStorage.put(1L, new Trainer("Tom", "Thompson", "tom.tompson", "abctgFdJQ5",
                true, 1L, "Heavy weight"));
        trainerStorage.put(2L, new Trainer("John", "Thompson", "john.tompson", "abctgFdJQ5",
                true, 2L, "Athletics"));

        trainingStorage.put(1L, new Training(1L, 1L, 1L, "Heavy weight exercise",
                new TrainingType("Heavy weight"), LocalDate.now(), 2L));
        trainingStorage.put(2L, new Training(2L, 2L, 2L, "Athletics exercise",
                new TrainingType("Athletics"), LocalDate.now(), 2L));
    }

    @Test
    public void testCreateTraining() {
        Training training = new Training(3L, 1L, 2L, "Athletics",
                new TrainingType("Athletics"), LocalDate.now(), 2L);
        trainingDAO.createTraining(training);

        assertEquals(3, storage.getTrainingStorage().size());
        assertTrue(storage.getTrainingStorage().containsKey(training.getTrainingId()));
    }

    @Test
    public void testGetTraining() {
        Training found = trainingDAO.getTraining(1L);

        assertEquals(2, storage.getTrainerStorage().size());
        assertTrue(trainerStorage.containsKey(found.getTrainingId()));
    }

    @Test
    public void testGetAllTrainings() {
        List<Training> found = trainingDAO.getAllTrainings();

        assertEquals(2, found.size());
        assertTrue(trainerStorage.containsKey(found.get(0).getTrainingId()));
        assertTrue(trainerStorage.containsKey(found.get(1).getTrainingId()));
    }
}
