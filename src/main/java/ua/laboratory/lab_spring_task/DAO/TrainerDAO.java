package ua.laboratory.lab_spring_task.DAO;

import ua.laboratory.lab_spring_task.Model.Trainer;

import java.util.List;

public interface TrainerDAO {
    Trainer createTrainer(Trainer trainer);
    Trainer getTrainer(Long id);
    List<Trainer> getAllTrainers();
    Trainer updateTrainer(Trainer trainer);
}
