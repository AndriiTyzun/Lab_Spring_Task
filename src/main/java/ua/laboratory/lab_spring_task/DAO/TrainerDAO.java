package ua.laboratory.lab_spring_task.DAO;

import ua.laboratory.lab_spring_task.Model.DTO.TrainerDTO;
import ua.laboratory.lab_spring_task.Model.Trainer;

import java.util.List;

public interface TrainerDAO {
    Trainer createOrUpdateTrainer(Trainer trainer);
    Trainer getTrainerById(Long id);
    TrainerDTO getTrainerDTOById(Long id);
    List<Trainer> getAllTrainers();
}
