package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Training;

import java.util.List;

public interface TrainingService {
    public Training createTraining(Training training);
    public Training getTraining(int id);
    public List<Training> getAllTrainings();
}
