package ua.laboratory.lab_spring_task.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.laboratory.lab_spring_task.dao.TraineeRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.response.TraineeProfileResponse;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.util.Utilities;
import ua.laboratory.lab_spring_task.util.exceptions.InvalidDataException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeRepository traineeRepository;
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public TraineeServiceImpl(TraineeRepository traineeRepository,
                              UserRepository userRepository) {
        this.traineeRepository = traineeRepository;
        this.userRepository = userRepository;
    }


    @Override
    public TraineeProfileResponse createTrainee(String firstName, String lastName,
                                                LocalDate dateOfBirth, String address,
                                                String password) {
        if(firstName == null || lastName == null)
            throw new InvalidDataException("Trainee cannot be null");

        logger.info("Creating trainee");
        User user = new User(firstName, lastName);
        Utilities.setUserUsername(user);
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);

        Trainee trainee = new Trainee(dateOfBirth, address);
        trainee.setUser(user);

        return new TraineeProfileResponse(traineeRepository.save(trainee));
    }

    @Override
    public TraineeProfileResponse updateTrainee(Trainee trainee) {
        if(trainee == null)
            throw new InvalidDataException("Trainee cannot be null");
        trainee.getUser().setPassword(passwordEncoder.encode(trainee.getUser().getPassword()));
        userRepository.save(trainee.getUser());
        return new TraineeProfileResponse(traineeRepository.save(trainee));
    }

    @Override
    public Boolean checkCredentials(Credentials credentials) {
        if(credentials.getUsername() == null || credentials.getPassword() == null ||
                credentials.getUsername().isEmpty() || credentials.getPassword().isEmpty()) {
            throw new InvalidDataException("Username and password are required");
        }

        return Utilities.checkCredentials(credentials.getUsername(),
                credentials.getPassword());
    }

    @Override
    public Trainee getTraineeById(Long id) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");

        logger.info("Fetching trainee with ID: {}", id);
        return traineeRepository.getReferenceById(id);
    }

    @Override
    public TraineeProfileResponse getTraineeByUsername(String username) {
        if(username == null)
            throw new InvalidDataException("Username cannot be null");

        logger.info("Fetching trainee with username: {}", username);
        return new TraineeProfileResponse(traineeRepository
                .getByUserUsername(username).orElseThrow());
    }

    @Override
    public TraineeProfileResponse changePassword(String username, String newPassword) {
        if(username == null || newPassword == null || username.isEmpty() || newPassword.isEmpty())
            throw new InvalidDataException("Username and password are required");

        Trainee trainee = traineeRepository.getByUserUsername(username).orElseThrow();
        trainee.getUser().setPassword(passwordEncoder.encode(newPassword));
        return new TraineeProfileResponse(traineeRepository.save(trainee));
    }

    @Override
    public void activateTrainee(Long id) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");

        User user = traineeRepository.getReferenceById(id).getUser();
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void activateTrainee(String username) {
        if(username == null)
            throw new InvalidDataException("Username cannot be null");

        User user = userRepository.getByUsername(username).orElseThrow();
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateTrainee(Long id) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");

        User user = traineeRepository.getReferenceById(id).getUser();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void deactivateTrainee(String username) {
        if(username == null)
            throw new InvalidDataException("Username cannot be null");

        User user = userRepository.getByUsername(username).orElseThrow();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void updateTrainers(Long id, Set<Trainer> trainers) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");
        if(trainers == null || trainers.isEmpty())
            throw new InvalidDataException("Trainers cannot be null or empty");

        Trainee trainee = traineeRepository.getTraineeById(id).orElseThrow();
        for (Trainer trainer : trainers)
            trainer.addTrainee(trainee);

        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);
    }

    @Override
    public Set<TraineeProfileResponse> getAllTrainees(Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new InvalidDataException("Username and password are required");

        logger.info("Fetching all trainees");
        return traineeRepository.getAllByOrderByIdDesc().stream()
                .map(TraineeProfileResponse::new).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteTrainee(Long id) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");

        logger.info("Deleting trainee with ID: {}", id);
        traineeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteTrainee(String username) {
        if(username.isEmpty())
            throw new InvalidDataException("Username cannot be empty");

        logger.info("Deleting trainee with username: {}", username);
        traineeRepository.deleteByUserUsername(username);
    }

    @Override
    public Set<TrainerProfileResponse> getAllTrainers(String username) {
        if(username.isEmpty())
            throw new InvalidDataException("Username cannot be empty");

        return traineeRepository.getAllTrainersByTraineeUsername(username)
                .stream().map(TrainerProfileResponse::new).collect(Collectors.toSet());
    }
}

