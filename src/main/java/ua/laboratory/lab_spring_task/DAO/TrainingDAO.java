package ua.laboratory.lab_spring_task.DAO;

import ua.laboratory.lab_spring_task.Model.Training;

import java.util.List;

public interface TrainingDAO {
    public Training createTraining(Training training);
    public Training getTraining(Long id);
    public List<Training> getAllTrainings();
}
