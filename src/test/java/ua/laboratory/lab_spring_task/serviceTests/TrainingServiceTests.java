package ua.laboratory.lab_spring_task.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.laboratory.lab_spring_task.dao.*;
import ua.laboratory.lab_spring_task.model.*;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.service.TrainingService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TrainingServiceTests {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    private Training testTraining;

    private Credentials validCredentials;
    private Trainer testTrainer;
    private Trainee testTrainee;
    private TrainingType testTrainingType;

    @BeforeEach
    public void setUp() {
        User user = new User("John", "Doe", "john.doe", "password123", true);
        userRepository.save(user);
        User userBackup = new User("Jane", "Doe", "jane.doe", "password123", true);
        userRepository.save(userBackup);
        testTrainingType = new TrainingType("Agility");
        testTrainingType = trainingTypeRepository.save(testTrainingType);

        testTrainer = new Trainer(trainingTypeRepository.getReferenceById(testTrainingType.getId()));
        testTrainer.setUser(user);
        testTrainer.getUser().setActive(true);
        trainerRepository.save(testTrainer);

        testTrainee = new Trainee(LocalDate.now(), "Address 1");
        testTrainee.setUser(userBackup);
        testTrainee.getUser().setActive(true);
        traineeRepository.save(testTrainee);

        testTraining = new Training("Training 1", LocalDate.now(), 1L,
                testTrainingType, testTrainee, testTrainer);
        trainingRepository.save(testTraining);

        validCredentials = new Credentials("john.doe", "password123");
    }

    @Test
    public void testCreateTraining() {
        Training createdTraining = trainingService.createTraining("Training 2", LocalDate.now(), 2L,
                testTrainingType, testTrainee, testTrainer);

        assertNotNull(createdTraining);
        assertNotNull(createdTraining.getId());
        assertEquals("Training 2", createdTraining.getTrainingName());
    }

    @Test
    public void testUpdateTraining() {
        testTraining.setTrainingName("Training 2");
        Training createdTraining = trainingService.updateTraining(testTraining);

        assertNotNull(createdTraining);
        assertNotNull(createdTraining.getId());
        assertEquals("Training 2", createdTraining.getTrainingName());
    }

    @Test
    public void testGetTraining() {
        Training retrievedTraining = trainingService.getTraining(testTraining.getId(), validCredentials);
        assertNotNull(retrievedTraining);
        assertEquals(testTraining.getId(), retrievedTraining.getId());
    }

    @Test
    public void testGetAllTrainings() {
        List<Training> trainings = trainingService.getAllTrainings(validCredentials);
        assertFalse(trainings.isEmpty());
    }

    @Test
    public void testGetTraineeTrainingsByCriteria() {
        List<Training> trainings = trainingService.getTraineeTrainingsByCriteria(
                testTrainee.getUser().getUsername(),
                null,
                null,
                null,
                null,
                validCredentials
        );

        assertFalse(trainings.isEmpty());
        assertEquals(testTraining.getTrainee().getUser().getUsername(), trainings.get(0).getTrainee().getUser().getUsername());
    }

    @Test
    public void testGetTrainerTrainingsByCriteria() {
        List<Training> trainings = trainingService.getTrainerTrainingsByCriteria(
                testTrainer.getUser().getUsername(),
                null,
                null,
                null,
                null,
                validCredentials
        );

        assertFalse(trainings.isEmpty());
        assertEquals(testTraining.getTrainer().getUser().getUsername(), trainings.get(0).getTrainer().getUser().getUsername());
    }
}
