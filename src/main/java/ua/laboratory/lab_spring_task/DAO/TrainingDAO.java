package ua.laboratory.lab_spring_task.DAO;

import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingDAO {
    Training createOrUpdateTraining(Training training);
    Training getTrainingById(Long id);
    List<Training> getAllTrainings();
    List<Training> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String trainerName);
    List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                 String traineeName);
}
