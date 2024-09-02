package ua.laboratory.lab_spring_task.Service;

import ua.laboratory.lab_spring_task.Model.Trainee;
import java.util.List;

public interface TraineeService {
    public Trainee createTrainee(Trainee trainee);
    public Trainee getTraineeById(int id);
    public Trainee getTraineeByUsername(String username);
    public List<Trainee> getAllTrainees();
    public Trainee updateTrainee(Trainee trainee);
    public void deleteTrainee(int id);
}
