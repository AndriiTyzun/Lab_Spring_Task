package ua.laboratory.lab_spring_task.DAO;

import ua.laboratory.lab_spring_task.Model.Trainer;

import java.util.List;

public interface TrainerDAO {
    public Trainer createTrainer(Trainer trainer);
    public Trainer getTrainer(Long id);
    public List<Trainer> getAllTrainers();
    public Trainer updateTrainer(Trainer trainer);
}
