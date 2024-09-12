package ua.laboratory.lab_spring_task.DAO;

import ua.laboratory.lab_spring_task.Model.Training;

import java.util.List;

public interface TrainingDAO {
    Training createTraining(Training training);
    Training getTraining(Long id);
    List<Training> getAllTrainings();
}
