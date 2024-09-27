package ua.laboratory.lab_spring_task.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.DTO.Credentials;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Service.TraineeService;
import ua.laboratory.lab_spring_task.util.Utilities;

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
            if(trainee == null)
                throw new IllegalArgumentException("Trainee cannot be null");

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
    public Boolean checkCredentials(Credentials credentials) {
        if(credentials.getUsername() == null || credentials.getPassword() == null ||
                credentials.getUsername().isEmpty() || credentials.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Username and password are required");
        }

        return Utilities.checkCredentials(credentials.getUsername(), credentials.getPassword(), traineeDAO);
    }

    @Override
    public Trainee getTraineeById(Long id, Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            if(id == null)
                throw new IllegalArgumentException("Id cannot be null");

            logger.info("Fetching trainee with ID: {}", id);
            return traineeDAO.getTraineeById(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }

    }

    @Override
    public Trainee getTraineeByUsername(String username, Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            if(username == null)
                throw new IllegalArgumentException("Username cannot be null");

            logger.info("Fetching trainee with username: {}", username);
            return traineeDAO.getTraineeByUsername(username);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Trainee changePassword(String username, String newPassword, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(username == null || newPassword == null || username.isEmpty() || newPassword.isEmpty())
            throw new IllegalArgumentException("Username and password are required");

        Trainee trainee = traineeDAO.getTraineeByUsername(username);
        trainee.getUser().setPassword(newPassword);
        return traineeDAO.createOrUpdateTrainee(trainee);
    }

    @Override
    public Trainee activateTrainee(Long id, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");

        Trainee trainee = traineeDAO.getTraineeById(id);
        trainee.getUser().setActive(true);
        return traineeDAO.createOrUpdateTrainee(trainee);
    }

    @Override
    public Trainee deactivateTrainee(Long id, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");

        Trainee trainee = traineeDAO.getTraineeById(id);
        trainee.getUser().setActive(false);
        return traineeDAO.createOrUpdateTrainee(trainee);
    }

    @Override
    public Trainee updateTrainers(Long id, Set<Trainer> trainers, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");
        if(trainers == null || trainers.isEmpty())
            throw new IllegalArgumentException("Trainers cannot be null or empty");

        Trainee trainee = traineeDAO.getTraineeById(id);
        trainee.setTrainers(trainers);
        return traineeDAO.createOrUpdateTrainee(trainee);
    }

    @Override
    public List<Trainee> getAllTrainees(Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            logger.info("Fetching all trainees");
            return traineeDAO.getAllTrainees();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public void deleteTrainee(Long id, Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            if(id == null)
                throw new IllegalArgumentException("Id cannot be null");

            logger.info("Deleting trainee with ID: {}", id);
            traineeDAO.deleteTrainee(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public void deleteTrainee(String username, Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            if(username == null)
                throw new IllegalArgumentException("Username cannot be null");

            logger.info("Deleting trainee with username: {}", username);
            traineeDAO.deleteTrainee(username);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}

