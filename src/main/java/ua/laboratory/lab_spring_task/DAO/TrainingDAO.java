package ua.laboratory.lab_spring_task.DAO;

import ua.laboratory.lab_spring_task.Model.Training;

import java.util.List;

public interface TrainingDAO {
    Training createOrUpdateTraining(Training training);
    Training getTrainingById(Long id);
    List<Training> getAllTrainings();
}
