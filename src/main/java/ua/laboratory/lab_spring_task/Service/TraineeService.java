package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.DTO.Credentials;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;

import java.util.List;
import java.util.Set;

public interface TraineeService {
    Trainee createOrUpdateTrainee(Trainee trainee);
    Boolean checkCredentials(Credentials credentials);
    Trainee getTraineeById(Long id, Credentials credentials);
    Trainee getTraineeByUsername(String username, Credentials credentials);
    Trainee changePassword(String username, String newPassword, Credentials credentials);
    Trainee activateTrainee(Long id, Credentials credentials);
    Trainee deactivateTrainee(Long id, Credentials credentials);
    Trainee updateTrainers(Long id, Set<Trainer> trainers, Credentials credentials);
    void deleteTrainee(Long id, Credentials credentials);
    void deleteTrainee(String username, Credentials credentials);
    List<Trainee> getAllTrainees(Credentials credentials);
}
