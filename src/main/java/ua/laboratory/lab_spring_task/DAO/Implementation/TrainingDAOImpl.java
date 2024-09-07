package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Util.InMemoryStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TrainingDAOImpl implements TrainingDAO {
    @Autowired
    private Map<Long, Training> trainingStorage;

    public TrainingDAOImpl(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Override
    public Training createTraining(Training training) {
        trainingStorage.put(training.getTrainingId(), training);
        return trainingStorage.get(training.getTrainingId());
    }

    @Override
    public Training getTraining(Long id) {
        return trainingStorage.get(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        return new ArrayList<>(trainingStorage.values());
    }
}
