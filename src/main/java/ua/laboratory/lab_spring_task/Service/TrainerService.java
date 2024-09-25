package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface TrainerService {
    Trainer createOrUpdateTrainer(Trainer trainer);
    Boolean checkCredentials(String username, String password);
    Trainer getTrainerById(Long id);
    Trainer getTrainerByUsername(String username);
    List<Trainer> getAllTrainers();
    Trainer changePassword(String username, String newPassword);
    Trainer activateTrainer(Long id);
    Trainer deactivateTrainer(Long id);
}
