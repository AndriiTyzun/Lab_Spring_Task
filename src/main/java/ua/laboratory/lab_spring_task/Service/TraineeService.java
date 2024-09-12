package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Trainee;
import java.util.List;

public interface TraineeService {
    Trainee createTrainee(Trainee trainee);
    Trainee getTraineeById(Long id);
    List<Trainee> getAllTrainees();
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(Long id);
}
