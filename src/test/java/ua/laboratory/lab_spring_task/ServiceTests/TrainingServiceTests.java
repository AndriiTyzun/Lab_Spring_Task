package ua.laboratory.lab_spring_task.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.Implementation.TraineeDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainerDAOImpl;
import ua.laboratory.lab_spring_task.DAO.Implementation.TrainingDAOImpl;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.Enum.TrainingType;
import ua.laboratory.lab_spring_task.Service.Implementation.TrainingServiceImpl;
import ua.laboratory.lab_spring_task.Service.TrainingService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrainingServiceTests {
    private TrainingDAO trainingDAO;
    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;
    private TrainingService trainingService;
    private Map<Long, Training> trainingStorage;
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Trainee> traineeStorage;

    public TrainingServiceTests() {
        traineeStorage = new HashMap<>();
        trainerStorage = new HashMap<>();
        trainingStorage = new HashMap<>();

        trainerDAO = new TrainerDAOImpl(trainerStorage);
        trainingDAO = new TrainingDAOImpl(trainingStorage);
        trainingService = new TrainingServiceImpl(trainingDAO);
    }


    @Test
    public void testCreateTraining() {
        Training training = new Training(3L, 1L, 2L, "Athletics",
                TrainingType.Agility, LocalDate.now(), 2L);
        trainingService.createTraining(training);

        assertEquals(3, trainingStorage.size());
        assertTrue(trainingStorage.containsKey(training.getId()));
    }

    @Test
    public void testGetTraining() {
        Training found = trainingService.getTraining(1L);

        assertEquals(2, trainingStorage.size());
        assertTrue(trainingStorage.containsKey(found.getId()));
    }

    @Test
    public void testGetAllTrainings() {
        List<Training> found = trainingService.getAllTrainings();

        assertEquals(2, found.size());
        assertTrue(trainingStorage.containsKey(found.get(0).getId()));
        assertTrue(trainingStorage.containsKey(found.get(1).getId()));
    }
}
