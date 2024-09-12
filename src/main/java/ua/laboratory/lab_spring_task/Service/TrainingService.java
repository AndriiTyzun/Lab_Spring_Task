package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Training;

import java.util.List;

public  interface TrainingService {
    Training createTraining(Training training);
    Training getTraining(Long id);
    List<Training> getAllTrainings();
}
