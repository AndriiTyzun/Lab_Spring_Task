package ua.laboratory.lab_spring_task.Util.Storage;

import org.springframework.stereotype.Component;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.Training;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryStorage {
    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Training> trainingStorage;

    public InMemoryStorage() {
        traineeStorage = new HashMap<>();
        trainerStorage = new HashMap<>();
        trainingStorage = new HashMap<>();
    }

    public Map<Long, Trainer> getTrainerStorage() {
        return trainerStorage;
    }

    public Map<Long, Trainee> getTraineeStorage() {
        return traineeStorage;
    }

    public Map<Long, Training> getTrainingStorage() {
        return trainingStorage;
    }
}
