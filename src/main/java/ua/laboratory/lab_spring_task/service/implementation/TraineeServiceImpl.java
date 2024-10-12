package ua.laboratory.lab_spring_task.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.laboratory.lab_spring_task.dao.TraineeRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.util.Utilities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeRepository traineeRepository;
    private final UserRepository userRepository;

    public TraineeServiceImpl(TraineeRepository traineeRepository, UserRepository userRepository) {
        this.traineeRepository = traineeRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Trainee createTrainee(String firstName, String lastName, LocalDate dateOfBirth, String address) {
        try {
            if(firstName == null || lastName == null)
                throw new IllegalArgumentException("Trainee cannot be null");
            logger.info("Creating trainee");

            User user = new User(firstName, lastName);
            Utilities.setUserUsername(user);
            user.setPassword(Utilities.generatePassword(10));
            user.setActive(true);

            Trainee trainee = new Trainee(dateOfBirth, address);
            trainee.setUser(user);

            return traineeRepository.save(trainee);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Trainee updateTrainee(Trainee trainee, Credentials credentials) {
        if(credentials.getUsername() == null || credentials.getPassword() == null ||
                credentials.getUsername().isEmpty() || credentials.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Username and password are required");
        }
        if(trainee == null)
            throw new IllegalArgumentException("Trainee cannot be null");

        userRepository.save(trainee.getUser());
        return traineeRepository.save(trainee);
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

        User user = traineeRepository.getReferenceById(id).getUser();
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void activateTrainee(String username, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(username == null)
            throw new IllegalArgumentException("Username cannot be null");

        User user = userRepository.getByUsername(username).orElseThrow(
                IllegalArgumentException::new
        );
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateTrainee(Long id, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");
        User user = traineeRepository.getReferenceById(id).getUser();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void deactivateTrainee(String username, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(username == null)
            throw new IllegalArgumentException("Username cannot be null");

        User user = userRepository.getByUsername(username).orElseThrow(
                IllegalArgumentException::new
        );
        user.setActive(false);
        userRepository.save(user);
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
    public Set<Trainee> getAllTrainees(Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            logger.info("Fetching all trainees");
            return new HashSet<>(traineeRepository.getAllByOrderByIdDesc());
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
            if(username.isEmpty())
                throw new IllegalArgumentException("Username cannot be empty");

            logger.info("Deleting trainee with username: {}", username);
            traineeRepository.deleteByUserUsername(username);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Set<Trainer> getAllTrainers(String username, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(username.isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");

        return traineeRepository.getAllTrainersByTraineeUsername(username);
    }
}

