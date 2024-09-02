package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Trainer;

import java.util.List;

public interface TrainerService {
    public Trainer createTrainer(Trainer trainer);
    public Trainer getTrainer(int id);
    public Trainer getTrainerByUsername(String username);
    public List<Trainer> getAllTrainers();
    public Trainer updateTrainer(Trainer trainer);
}
