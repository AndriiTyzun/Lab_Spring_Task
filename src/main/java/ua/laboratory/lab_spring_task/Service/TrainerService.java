package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface TrainerService {
    Trainer createTrainer(Trainer trainer) throws NoSuchAlgorithmException;
    Trainer getTrainer(Long id);
    List<Trainer> getAllTrainers();
    Trainer updateTrainer(Trainer trainer);
}
