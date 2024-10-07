package ua.laboratory.lab_spring_task.service;

import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer createTrainer(String firstName, String lastName, TrainingType trainingType);
    Trainer updateTrainer(Trainer trainer);
    Boolean checkCredentials(Credentials credentials);
    Trainer getTrainerById(Long id, Credentials credentials);
    Trainer getTrainerByUsername(String username, Credentials credentials);
    List<Trainer> getAllTrainers(Credentials credentials);
    Trainer changePassword(String username, String newPassword, Credentials credentials);
    void activateTrainer(Long id, Credentials credentials);
    void deactivateTrainer(Long id, Credentials credentials);
    List<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername, Credentials credentials);
}
