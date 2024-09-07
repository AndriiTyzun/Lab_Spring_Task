package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
        logger.info("Creating trainee with ID: {}", trainee.getUserId());

        traineeStorage.put(trainee.getUserId(), trainee);
        return traineeStorage.get(trainee.getUserId());
    }

    @Override
    public Trainee getTraineeById(Long id) {
        logger.debug("Fetching trainee with ID: {}", id);
        return traineeStorage.get(id);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        logger.debug("Fetching all trainees");
        return new ArrayList<>(traineeStorage.values());
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Updating trainee with ID: {}", trainee.getUserId());
        traineeStorage.put(trainee.getUserId(), trainee);
        return traineeStorage.get(trainee.getUserId());
    }

    @Override
    public Trainee deleteTrainee(Long id) {
        logger.info("Deleting trainee with ID: {}", id);
        return traineeStorage.remove(id);
    }
}
