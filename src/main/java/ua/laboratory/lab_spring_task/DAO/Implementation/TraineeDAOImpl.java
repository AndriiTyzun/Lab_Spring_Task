package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Util.Storage.InMemoryStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    private final Map<Long, Trainee> traineeStorage;

    @Autowired
    public TraineeDAOImpl(InMemoryStorage storage) {
        traineeStorage = storage.getTraineeStorage();
    }

    @Override
    public Trainee createTrainee(Trainee trainee) {
        traineeStorage.put(trainee.getUserId(), trainee);
        return traineeStorage.get(trainee.getUserId());
    }

    @Override
    public Trainee getTraineeById(Long id) {
        return traineeStorage.get(id);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return new ArrayList<>(traineeStorage.values());
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        traineeStorage.put(trainee.getUserId(), trainee);
        return traineeStorage.get(trainee.getUserId());
    }

    @Override
    public Trainee deleteTrainee(Long id) {
        return traineeStorage.remove(id);
    }
}
