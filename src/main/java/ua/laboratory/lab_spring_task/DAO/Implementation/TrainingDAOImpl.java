package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(TrainingDAOImpl.class);

    @Autowired
    private Map<Long, Training> trainingStorage;

    public TrainingDAOImpl(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Override
    public Training createTraining(Training training) {
        logger.info("Creating training with ID: {}", training.getTrainingId());
        trainingStorage.put(training.getTrainingId(), training);
        return trainingStorage.get(training.getTrainingId());
    }

    @Override
    public Training getTraining(Long id) {
        logger.debug("Fetching training with ID: {}", id);
        return trainingStorage.get(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        logger.debug("Fetching all trainings");
        return new ArrayList<>(trainingStorage.values());
    }
}
