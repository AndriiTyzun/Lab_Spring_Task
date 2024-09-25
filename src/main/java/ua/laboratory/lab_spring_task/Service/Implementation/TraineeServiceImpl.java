package ua.laboratory.lab_spring_task.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Service.TraineeService;
import ua.laboratory.lab_spring_task.Util.Utilities;

import java.util.List;
import java.util.Set;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeDAO traineeDAO;

    public TraineeServiceImpl(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }


    @Override
    public Trainee createOrUpdateTrainee(Trainee trainee) {
        try {
            logger.info("Creating trainee with ID: {}", trainee.getId());
            Utilities.setTraineeUsername(trainee, traineeDAO);
            trainee.getUser().setPassword(Utilities.generatePassword(10));

            return traineeDAO.createOrUpdateTrainee(trainee);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Boolean checkCredentials(String username, String password) {
        if(username == null || password == null) {
            throw new IllegalArgumentException("Username and password are required");
        }
        if(username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password are required");
        }
        return Utilities.checkCredentials(username, password, traineeDAO);
    }

    @Override
    public Trainee getTraineeById(Long id) {
        try {
            logger.info("Fetching trainee with ID: {}", id);
            return traineeDAO.getTraineeById(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }

    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        try {
            logger.info("Fetching trainee with username: {}", username);
            return traineeDAO.getTraineeByUsername(username);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Trainee changePassword(String username, String newPassword) {
        Trainee trainee = traineeDAO.getTraineeByUsername(username);
        trainee.getUser().setPassword(newPassword);
        return traineeDAO.createOrUpdateTrainee(trainee);
    }

    @Override
    public Trainee activateTrainee(Long id) {
        Trainee trainee = traineeDAO.getTraineeById(id);
        trainee.getUser().setActive(true);
        return traineeDAO.createOrUpdateTrainee(trainee);
    }

    @Override
    public Trainee deactivateTrainee(Long id) {
        Trainee trainee = traineeDAO.getTraineeById(id);
        trainee.getUser().setActive(false);
        return traineeDAO.createOrUpdateTrainee(trainee);
    }

    @Override
    public Trainee updateTrainers(Long id, Set<Trainer> trainers) {
        Trainee trainee = traineeDAO.getTraineeById(id);
        trainee.setTrainers(trainers);
        return traineeDAO.createOrUpdateTrainee(trainee);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        try {
            logger.info("Fetching all trainees");
            return traineeDAO.getAllTrainees();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public void deleteTrainee(Long id) {
        try {
            logger.info("Deleting trainee with ID: {}", id);
            traineeDAO.deleteTrainee(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public void deleteTrainee(String username) {
        try {
            logger.info("Deleting trainee with username: {}", username);
            traineeDAO.deleteTrainee(username);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}

