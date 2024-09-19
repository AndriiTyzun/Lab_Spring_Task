package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAOImpl.class);

    @Autowired
    private Map<Long, Trainee> traineeStorage;

    public TraineeDAOImpl(Map<Long, Trainee> traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Override
    public Trainee createTrainee(Trainee trainee) {
        try {
            logger.info("Creating trainee with ID: {}", trainee.getTraineeId());

            traineeStorage.put(trainee.getTraineeId(), trainee);
            return traineeStorage.get(trainee.getTraineeId());
        }catch (Exception e) {
            logger.error("Failed to create trainee with ID: {}", trainee.getTraineeId());
            throw new RuntimeException("Failed to create trainee with ID: " + trainee.getTraineeId(), e);
        }
    }

    @Override
    public Trainee getTraineeById(Long id) {
        try {
            logger.info("Fetching trainee with ID: {}", id);
            return traineeStorage.get(id);
        }catch (Exception e) {
            logger.error("Failed to fetch trainee with ID: {}", id);
            throw new RuntimeException("Failed to fetch trainee with ID: " + id, e);
        }
    }

    @Override
    public List<Trainee> getAllTrainees() {
        try {
            logger.info("Fetching all trainees");
            return new ArrayList<>(traineeStorage.values());
        } catch (Exception e){
            logger.error("Failed to fetch all trainees", e);
            throw new RuntimeException("Failed to fetch all trainees", e);
        }
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        try {
            logger.info("Updating trainee with ID: {}", trainee.getTraineeId());
            traineeStorage.put(trainee.getTraineeId(), trainee);
            return traineeStorage.get(trainee.getTraineeId());
        } catch (Exception e){
            logger.error("Failed to update trainee with ID: {}", trainee.getTraineeId());
            throw new RuntimeException("Failed to update trainee with ID: " + trainee.getTraineeId(), e);
        }
    }

    @Override
    public Trainee deleteTrainee(Long id) {
        try {
            logger.info("Deleting trainee with ID: {}", id);
            return traineeStorage.remove(id);
        } catch (Exception e){
            logger.error("Failed to delete trainee with ID: {}", id);
            throw new RuntimeException("Failed to delete trainee with ID: " + id, e);
        }
    }
}
