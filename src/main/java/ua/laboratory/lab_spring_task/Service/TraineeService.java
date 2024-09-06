package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Trainee;
import java.util.List;

public interface TraineeService {
    public Trainee createTrainee(Trainee trainee);
    public Trainee getTraineeById(Long id);
    public List<Trainee> getAllTrainees();
    public Trainee updateTrainee(Trainee trainee);
    public void deleteTrainee(Long id);
}
