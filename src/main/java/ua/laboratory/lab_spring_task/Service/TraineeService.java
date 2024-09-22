package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Trainee;
import java.util.List;

public interface TraineeService {
    Trainee createOrUpdateTrainee(Trainee trainee);
    Trainee getTraineeById(Long id);
    List<Trainee> getAllTrainees();
    void deleteTrainee(Long id);
}
