package ua.laboratory.lab_spring_task.service;

import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainer;

import java.util.List;
import java.util.Set;

public interface TrainerService {
    Trainer createTrainer(String firstName, String lastName, TrainingType trainingType, String password);
    Trainer updateTrainer(Trainer trainer);
    Boolean checkCredentials(Credentials credentials);
    Trainer getTrainerById(Long id);
    Trainer getTrainerByUsername(String username);
    List<Trainer> getAllTrainers();
    Trainer changePassword(String username, String newPassword);
    void updateTrainees(Long id, Set<Trainee> trainees);
    void activateTrainer(Long id);
    void activateTrainer(String username);
    void deactivateTrainer(Long id);
    void deactivateTrainer(String username);
    Set<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername);
    Set<Trainee> getAllTrainees(String username);
}
