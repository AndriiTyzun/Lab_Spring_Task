package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.DTO.Credentials;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public  interface TrainingService {
    Training createOrUpdateTraining(Training training);
    Training getTraining(Long id, Credentials credentials);
    List<Training> getAllTrainings(Credentials credentials);
    List<Training> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String trainerName, Credentials credentials);
    List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String traineeName, Credentials credentials);
}
