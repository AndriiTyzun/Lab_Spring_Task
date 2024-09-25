package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public  interface TrainingService {
    Training createOrUpdateTraining(Training training);
    Training getTraining(Long id);
    List<Training> getAllTrainings();
    List<Training> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String trainerName, TrainingType trainingType);
    List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String traineeName, TrainingType trainingType);
}
