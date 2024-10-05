package ua.laboratory.lab_spring_task.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.dao.TraineeRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.util.Utilities;

import java.util.List;
import java.util.Set;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeRepository traineeRepository;

    public TraineeServiceImpl(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }


    @Override
    public Trainee createOrUpdateTrainee(Trainee trainee) {
        try {
            if(trainee == null)
                throw new IllegalArgumentException("Trainee cannot be null");

            logger.info("Creating trainee with ID: {}", trainee.getId());

            Utilities.setUserUsername(trainee.getUser());
            trainee.getUser().setPassword(Utilities.generatePassword(10));

            return traineeRepository.save(trainee);
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

        return Utilities.checkCredentials(credentials.getUsername(), credentials.getPassword());
    }

    @Override
    public Trainee getTraineeById(Long id, Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            if(id == null)
                throw new IllegalArgumentException("Id cannot be null");

            logger.info("Fetching trainee with ID: {}", id);
            return traineeRepository.getReferenceById(id);
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
            return traineeRepository.getByUserUsername(username).orElseThrow(
                    IllegalArgumentException::new
            );
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

        Trainee trainee = traineeRepository.getByUserUsername(username).orElseThrow(
                IllegalArgumentException::new
        );
        trainee.getUser().setPassword(newPassword);
        return traineeRepository.save(trainee);
    }

    @Override
    public void activateTrainee(Long id, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");

        traineeRepository.updateUserActiveStatusByTraineeId(id, true);
    }

    @Override
    public void deactivateTrainee(Long id, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");

        traineeRepository.updateUserActiveStatusByTraineeId(id, false);
    }

    @Override
    public void updateTrainers(Long id, Set<Trainer> trainers, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");
        if(trainers == null || trainers.isEmpty())
            throw new IllegalArgumentException("Trainers cannot be null or empty");

        Trainee trainee = traineeRepository.getReferenceById(id);
        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);
    }

    @Override
    public List<Trainee> getAllTrainees(Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            logger.info("Fetching all trainees");
            return traineeRepository.getAllByOrderByIdDesc();
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
            traineeRepository.deleteById(id);
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
            traineeRepository.deleteByUserUsername(username);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}

