package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;

import java.util.List;
import java.util.Set;

public interface TraineeService {
    Trainee createOrUpdateTrainee(Trainee trainee);
    Boolean checkCredentials(String username, String password);
    Trainee getTraineeById(Long id);
    Trainee getTraineeByUsername(String username);
    Trainee changePassword(String username, String newPassword);
    Trainee activateTrainee(Long id);
    Trainee deactivateTrainee(Long id);
    Trainee updateTrainers(Long id, Set<Trainer> trainers);
    void deleteTrainee(Long id);
    void deleteTrainee(String username);
    List<Trainee> getAllTrainees();
}
