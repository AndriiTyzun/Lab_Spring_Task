package ua.laboratory.lab_spring_task.DAOTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.laboratory.lab_spring_task.DAO.Implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainerDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainingDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.TrainingType;
import ua.laboratory.lab_spring_task.Util.InMemoryStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainingDAOTests {
    private TrainingDAO trainingDAO;
    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;
    private Map<Long, Training> trainingStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Trainee> traineeStorage;

    public TrainingDAOTests() {
        traineeStorage = new HashMap<>();
        trainerStorage = new HashMap<>();
        trainingStorage = new HashMap<>();
        traineeDAO = new TraineeDAOImpl(traineeStorage);
        trainerDAO = new TrainerDAOImpl(trainerStorage);
        trainingDAO = new TrainingDAOImpl(trainingStorage);
    }

    @BeforeEach
    public void setUp() {
        trainingStorage.clear();
        traineeStorage.clear();
        trainerStorage.clear();

        traineeStorage.put(1L, new Trainee("Tom", "Thompson",
                true, 1L, LocalDate.now(), "City, Street, House 1"));
        traineeStorage.put(2L, new Trainee("John", "Thompson",
                true, 2L, LocalDate.now(), "City, Street, House 2"));
        trainerStorage.put(1L, new Trainer("Tom", "Thompson",
                true, 1L, "Heavy weight"));
        trainerStorage.put(2L, new Trainer("John", "Thompson",
                true, 2L, "Athletics"));

        trainingStorage.put(1L, new Training(1L, 1L, 1L, "Heavy weight exercise",
                TrainingType.HEAVYWEIGHT, LocalDate.now(), 2L));
        trainingStorage.put(2L, new Training(2L, 2L, 2L, "Athletics exercise",
                TrainingType.ATHLETICS, LocalDate.now(), 2L));
    }

    @Test
    public void testCreateTraining() {
        Training training = new Training(3L, 1L, 2L, "Athletics",
                TrainingType.ATHLETICS, LocalDate.now(), 2L);
        trainingDAO.createTraining(training);

        assertEquals(3, trainingStorage.size());
        assertTrue(trainingStorage.containsKey(training.getTrainingId()));
    }

    @Test
    public void testGetTraining() {
        Training found = trainingDAO.getTraining(1L);

        assertEquals(2, trainingStorage.size());
        assertTrue(trainingStorage.containsKey(found.getTrainingId()));
    }

    @Test
    public void testGetAllTrainings() {
        List<Training> found = trainingDAO.getAllTrainings();

        assertEquals(2, found.size());
        assertTrue(trainingStorage.containsKey(found.get(0).getTrainingId()));
        assertTrue(trainingStorage.containsKey(found.get(1).getTrainingId()));
    }
}
