package ua.laboratory.lab_spring_task.service;

import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Training;

import java.time.LocalDate;
import java.util.List;

public  interface TrainingService {
    Training createTraining(String trainingName, LocalDate trainingDate, Long trainingDuration,
                            TrainingType trainingType, Trainee trainee, Trainer trainer);
    Training updateTraining(Training training);
    Training getTraining(Long id, Credentials credentials);
    List<Training> getAllTrainings(Credentials credentials);
    List<Training> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String trainerName, Credentials credentials);
    List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String traineeName, Credentials credentials);
}
