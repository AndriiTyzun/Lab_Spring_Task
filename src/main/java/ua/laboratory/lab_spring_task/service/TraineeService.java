package ua.laboratory.lab_spring_task.service;

import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.response.TraineeProfileResponse;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface TraineeService {
    TraineeProfileResponse createTrainee(String firstName, String lastName, LocalDate dateOfBirth, String address, String password);
    TraineeProfileResponse updateTrainee(Trainee trainee);
    Boolean checkCredentials(Credentials credentials);
    Trainee getTraineeById(Long id);
    TraineeProfileResponse getTraineeByUsername(String username);
    TraineeProfileResponse changePassword(String username, String newPassword);
    void activateTrainee(Long id);
    void activateTrainee(String username);
    void deactivateTrainee(Long id);
    void deactivateTrainee(String username);
    void updateTrainers(Long id, Set<Trainer> trainers);
    void deleteTrainee(Long id);
    void deleteTrainee(String username);
    Set<TraineeProfileResponse> getAllTrainees(Credentials credentials);
    Set<TrainerProfileResponse> getAllTrainers(String username);
}
