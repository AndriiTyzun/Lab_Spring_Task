package ua.laboratory.lab_spring_task.DAO;

import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.Model.Trainee;

import java.util.List;

@Repository
public interface TraineeDAO {
    Trainee createOrUpdateTrainee(Trainee trainee);
    Trainee getTraineeById(Long id);
    List<Trainee> getAllTrainees();
    void deleteTrainee(Long id);
}

