package ua.laboratory.lab_spring_task.service_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ua.laboratory.lab_spring_task.dao.TraineeRepository;
import ua.laboratory.lab_spring_task.dao.TrainerRepository;
import ua.laboratory.lab_spring_task.dao.TrainingTypeRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.service.TraineeService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TraineeServiceTests {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    private Credentials validCredentials;
    private Trainee testTrainee;
    private Trainer testTrainer;
    private TrainingType testTrainingType;

    @BeforeEach
    public void setup() {
        User user = new User("John", "Smith", "john.smith", "password123", true);
        userRepository.save(user);
        User userBackup = new User("Jane", "Smith", "jane.smith", "password123", true);
        userRepository.save(userBackup);

        testTrainee = new Trainee(LocalDate.now(), "Address 1");
        testTrainee.setUser(user);
        testTrainee.getUser().setActive(true);

        traineeRepository.save(testTrainee);

        validCredentials = new Credentials("john.smith", "password123");

        testTrainingType = new TrainingType("Test type 1");
        testTrainingType = trainingTypeRepository.save(testTrainingType);
        testTrainer = new Trainer(trainingTypeRepository.getReferenceById(testTrainingType.getId()));
        testTrainer.setUser(user);
        testTrainer.getUser().setActive(true);

        trainerRepository.save(testTrainer);
    }

    @Test
    public void testCreateTrainee() {
        Trainee createdTrainee = traineeService.createTrainee("Jason", "Smith", LocalDate.now(), "Address 1");

        assertNotNull(createdTrainee);
        assertEquals("jason.smith", createdTrainee.getUser().getUsername());
        assertEquals("Address 1", createdTrainee.getAddress());
    }

    @Test
    public void testCheckCredentials_Valid() {
        Boolean isValid = traineeService.checkCredentials(validCredentials);
        assertTrue(isValid);
    }

    @Test
    public void testCheckCredentials_Invalid() {
        Credentials invalidCredentials = new Credentials("johnsmith", "wrongpassword");
        assertThrows(IllegalArgumentException.class,
                () -> traineeService.checkCredentials(invalidCredentials));
    }

    @Test
    public void testGetTraineeById() {
        Trainee retrievedTrainee = traineeService.getTraineeById(testTrainee.getId(), validCredentials);
        assertNotNull(retrievedTrainee);
        assertEquals("John", retrievedTrainee.getUser().getFirstName());
    }

    @Test
    public void testGetTraineeByUsername() {
        Trainee retrievedTrainee = traineeService.getTraineeByUsername("john.smith", validCredentials);
        assertNotNull(retrievedTrainee);
        assertEquals("John", retrievedTrainee.getUser().getFirstName());
    }

    @Test
    public void testChangePassword() {
        traineeService.changePassword("john.smith", "newpassword", validCredentials);
        Credentials newCredentials = new Credentials("john.smith", "newpassword");
        assertTrue(traineeService.checkCredentials(newCredentials));
    }

    @Test
    public void testActivateTrainee() {
        traineeService.activateTrainee(testTrainee.getId(), validCredentials);
        Trainee activatedTrainee = traineeService.getTraineeById(testTrainee.getId(), validCredentials);
        assertTrue(activatedTrainee.getUser().isActive());
    }

    @Test
    public void testDeactivateTrainee() {
        traineeService.deactivateTrainee(testTrainee.getId(), validCredentials);
        Trainee deactivatedTrainee = traineeService.getTraineeById(testTrainee.getId(), validCredentials);
        assertFalse(deactivatedTrainee.getUser().isActive());
    }

    @Test
    public void testDeleteTraineeById() {
        traineeService.deleteTrainee(testTrainee.getId(), validCredentials);
        Optional<Trainee> deletedTrainee = traineeRepository.findById(testTrainee.getId());
        assertTrue(deletedTrainee.isEmpty());
    }

    @Test
    public void testDeleteTraineeByUsername() {
        traineeService.deleteTrainee("john.smith", validCredentials);
        Optional<Trainee> deletedTrainee = traineeRepository.findById(testTrainee.getId());
        assertTrue(deletedTrainee.isEmpty());
    }

    @Test
    public void testGetAllTrainees() {
        Set<Trainee> allTrainees = traineeService.getAllTrainees(validCredentials);
        assertNotNull(allTrainees);
        assertFalse(allTrainees.isEmpty());
    }

    @Test
    public void testUpdateTrainers() {
        Set<Trainer> trainers = new HashSet<>();
        Set<Trainee> trainees = new HashSet<>();
        trainers.add(testTrainer);
        trainees.add(testTrainee);
        testTrainer.setTrainees(trainees);
        testTrainee.setTrainers(trainers);
        trainerRepository.save(testTrainer);

        traineeService.updateTrainers(testTrainee.getId(), trainers, validCredentials);
        Trainee updatedTrainee = traineeService.getTraineeById(testTrainee.getId(), validCredentials);

        assertNotNull(updatedTrainee.getTrainers());
        assertTrue(updatedTrainee.getTrainers().contains(testTrainer));
    }

}
