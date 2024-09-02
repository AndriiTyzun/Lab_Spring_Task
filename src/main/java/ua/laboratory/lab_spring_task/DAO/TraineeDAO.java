package ua.laboratory.lab_spring_task.DAO;

import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.Model.Trainee;

import java.util.List;

@Repository
public interface TraineeDAO {
    public Trainee createTrainee(Trainee trainee);
    public Trainee getTraineeById(Long id);
    public List<Trainee> getAllTrainees();
    public Trainee updateTrainee(Trainee trainee);
    public Trainee deleteTrainee(Long id);
}

