package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.Model.Training;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@Repository
public class TrainingDAOImpl implements TrainingDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainingDAOImpl.class);

    @Autowired
    private Map<Long, Training> trainingStorage;

    public TrainingDAOImpl(Map<Long, Training> trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    @Override
    public Training createTraining(Training training) {
        try {
            logger.info("Creating training with ID: {}", training.getId());
            trainingStorage.put(training.getId(), training);
            return trainingStorage.get(training.getId());
        }catch (Exception e) {
            logger.error("Failed to create training with ID: {}", training.getId());
            throw new RuntimeException("Failed to create training with ID: " + training.getId(), e);
        }
    }

    @Override
    public Training getTraining(Long id) {
        try {
            logger.info("Fetching training with ID: {}", id);
            return trainingStorage.get(id);
        } catch (Exception e) {
            logger.error("Failed to fetch training with ID: {}", id);
            throw new RuntimeException("Failed to fetch training with ID: " + id, e);
        }
    }

    @Override
    public List<Training> getAllTrainings() {
        try {
            logger.info("Fetching all trainings");
            return new ArrayList<>(trainingStorage.values());
        } catch (Exception e) {
            logger.error("Failed to fetch all trainings", e);
            throw new RuntimeException("Failed to fetch all trainings", e);
        }
    }
}
