package ua.laboratory.lab_spring_task.service;

import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainer;

import java.util.List;
import java.util.Set;

public interface TrainerService {
    Trainer createTrainer(String firstName, String lastName, TrainingType trainingType);
    Trainer updateTrainer(Trainer trainer, Credentials credentials);
    Boolean checkCredentials(Credentials credentials);
    Trainer getTrainerById(Long id, Credentials credentials);
    Trainer getTrainerByUsername(String username, Credentials credentials);
    List<Trainer> getAllTrainers(Credentials credentials);
    Trainer changePassword(String username, String newPassword, Credentials credentials);
    void updateTrainees(Long id, Set<Trainee> trainees, Credentials credentials);
    void activateTrainer(Long id, Credentials credentials);
    void activateTrainer(String username, Credentials credentials);
    void deactivateTrainer(Long id, Credentials credentials);
    void deactivateTrainer(String username, Credentials credentials);
    Set<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername, Credentials credentials);
    Set<Trainee> getAllTrainees(String username, Credentials credentials);
}
