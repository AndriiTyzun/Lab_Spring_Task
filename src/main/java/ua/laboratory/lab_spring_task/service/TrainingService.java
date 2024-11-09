package ua.laboratory.lab_spring_task.service;

import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Training;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;
import ua.laboratory.lab_spring_task.model.response.TrainingDetailsResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public  interface TrainingService {
    TrainingDetailsResponse createTraining(String trainingName, LocalDate trainingDate, Long trainingDuration,
                                                           TrainingType trainingType, Trainee trainee, Trainer trainer);
    TrainingDetailsResponse updateTraining(Training training);
    TrainingDetailsResponse getTraining(Long id);
    Set<TrainingDetailsResponse> getAllTrainings();
    Set<TrainingType> getAllTrainingTypes();
    Set<TrainingDetailsResponse> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String trainerName,String trainingType);
    Set<TrainingDetailsResponse> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String traineeName,String trainingType);
}
