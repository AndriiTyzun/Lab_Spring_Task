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
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.service.TrainerService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TrainerServiceTests {
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    private Credentials validCredentials;
    private Trainer testTrainer;
    private Trainee testTrainee;
    private TrainingType testTrainingType;

    @BeforeEach
    public void setup() {
        User user = new User("John", "Smith", "john.smith", "password123", true);
        userRepository.save(user);
        User userBackup = new User("Jane", "Smith", "jane.smith", "password123", true);
        userRepository.save(userBackup);
        testTrainingType = new TrainingType("Test type 1");
        testTrainingType = trainingTypeRepository.save(testTrainingType);

        testTrainer = new Trainer(trainingTypeRepository.getReferenceById(testTrainingType.getId()));
        testTrainer.setUser(user);
        testTrainer.getUser().setActive(true);

        trainerRepository.save(testTrainer);
        validCredentials = new Credentials("john.smith", "password123");

        testTrainee = new Trainee(LocalDate.now(), "Address 1");
        testTrainee.setUser(userBackup);
        testTrainee.getUser().setActive(true);
        traineeRepository.save(testTrainee);
    }

    @Test
    public void testCreateTrainer() {
        Trainer newTrainer = trainerService.createTrainer("Jane", "Smith", new TrainingType("Agility"));

        assertNotNull(newTrainer);
        assertEquals("Jane", newTrainer.getUser().getFirstName());
        assertEquals("Agility", newTrainer.getSpecialization().getTrainingTypeName());
    }

    @Test
    public void testUpdateTrainer() {
        testTrainer.getUser().setLastName("UpdatedLastName");
        Trainer updatedTrainer = trainerService.updateTrainer(testTrainer, validCredentials);

        assertNotNull(updatedTrainer);
        assertEquals("UpdatedLastName", updatedTrainer.getUser().getLastName());
    }

    @Test
    public void testCheckCredentials_Valid() {
        Boolean isValid = trainerService.checkCredentials(validCredentials);
        assertTrue(isValid);
    }

    @Test
    public void testCheckCredentials_Invalid() {
        Credentials invalidCredentials = new Credentials("johnsmith", "wrongpassword");
        assertThrows(IllegalArgumentException.class,
                () -> trainerService.checkCredentials(invalidCredentials));
    }

    @Test
    public void testGetTrainerById() {
        Trainer trainer = trainerService.getTrainerById(testTrainer.getId(), validCredentials);
        assertNotNull(trainer);
        assertEquals(testTrainer.getId(), trainer.getId());
    }

    @Test
    public void testGetTrainerByUsername() {
        Trainer trainer = trainerService.getTrainerByUsername(testTrainer.getUser().getUsername(), validCredentials);
        assertNotNull(trainer);
        assertEquals(testTrainer.getUser().getUsername(), trainer.getUser().getUsername());
    }

    @Test
    public void testGetAllTrainers() {
        List<Trainer> trainers = trainerService.getAllTrainers(validCredentials);
        assertNotNull(trainers);
        assertFalse(trainers.isEmpty());
    }

    @Test
    public void testChangePassword() {
        trainerService.changePassword(testTrainer.getUser().getUsername(), "newPassword", validCredentials);

        Credentials updatedCredentials = new Credentials(testTrainer.getUser().getUsername(), "newPassword");
        Boolean isUpdated = trainerService.checkCredentials(updatedCredentials);

        assertTrue(isUpdated);
    }

    @Test
    public void testActivateTrainer() {
        trainerService.activateTrainer(testTrainer.getId(), validCredentials);

        Trainer activatedTrainer = trainerService.getTrainerById(testTrainer.getId(), validCredentials);
        assertTrue(activatedTrainer.getUser().isActive());
    }

    @Test
    public void testDeactivateTrainer() {
        trainerService.deactivateTrainer(testTrainer.getId(), validCredentials);

        Trainer deactivatedTrainer = trainerService.getTrainerById(testTrainer.getId(), validCredentials);
        assertFalse(deactivatedTrainer.getUser().isActive());
    }

    @Test
    public void testGetUnassignedTrainersByTraineeUsername() {
        Set<Trainer> unassignedTrainers = trainerService.getUnassignedTrainersByTraineeUsername(
                testTrainee.getUser().getUsername(), validCredentials);

        assertNotNull(unassignedTrainers);
        assertFalse(unassignedTrainers.isEmpty());
    }

}
