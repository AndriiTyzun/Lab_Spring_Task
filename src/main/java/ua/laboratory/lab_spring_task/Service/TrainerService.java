package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.DTO.Credentials;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface TrainerService {
    Trainer createOrUpdateTrainer(Trainer trainer);
    Boolean checkCredentials(Credentials credentials);
    Trainer getTrainerById(Long id, Credentials credentials);
    Trainer getTrainerByUsername(String username, Credentials credentials);
    List<Trainer> getAllTrainers(Credentials credentials);
    Trainer changePassword(String username, String newPassword, Credentials credentials);
    Trainer activateTrainer(Long id, Credentials credentials);
    Trainer deactivateTrainer(Long id, Credentials credentials);
    List<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername, Credentials credentials);
}
