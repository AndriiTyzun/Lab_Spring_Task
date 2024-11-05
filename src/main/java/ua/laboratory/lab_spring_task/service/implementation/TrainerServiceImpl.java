package ua.laboratory.lab_spring_task.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.dao.TrainerRepository;
import ua.laboratory.lab_spring_task.dao.TrainingTypeRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.util.Utilities;
import ua.laboratory.lab_spring_task.util.exceptions.InvalidDataException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserRepository userRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository, UserRepository userRepository) {
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Trainer createTrainer(String firstName, String lastName, TrainingType trainingType) {
        if(firstName == null || lastName == null || trainingType == null)
            throw new InvalidDataException("Trainer cannot be null");

        logger.info("Creating trainer");
        logger.info("Creating trainee");

        TrainingType type = trainingTypeRepository.getByTrainingTypeName(
                trainingType.getTrainingTypeName()).orElseThrow();

        User user = new User(firstName, lastName);
        Utilities.setUserUsername(user);
        user.setPassword(Utilities.generatePassword(10));
        user.setActive(true);

        Trainer trainer = new Trainer(type);
        trainer.setUser(user);

        return trainerRepository.save(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        if(trainer == null)
            throw new InvalidDataException("Trainee cannot be null");

        trainer.setSpecialization(trainingTypeRepository.getByTrainingTypeName(
                trainer.getSpecialization().getTrainingTypeName()).orElseThrow());

        userRepository.save(trainer.getUser());
        return trainerRepository.save(trainer);
    }

    @Override
    public Boolean checkCredentials(Credentials credentials) {
        if(credentials.getUsername() == null || credentials.getPassword() == null ||
                credentials.getUsername().isEmpty() || credentials.getPassword().isEmpty())
            throw new InvalidDataException("Username and password are required");

        return Utilities.checkCredentials(credentials.getUsername(), credentials.getPassword());
    }

    @Override
    public Trainer getTrainerById(Long id) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");

        logger.info("Fetching trainer with ID: {}", id);
        return trainerRepository.getReferenceById(id);
    }

    @Override
    public Trainer getTrainerByUsername(String username) {
        if(username == null)
            throw new InvalidDataException("Username cannot be null");

        logger.info("Fetching trainer with username: {}", username);
        return trainerRepository.getByUserUsername(username).orElseThrow();
    }

    @Override
    public List<Trainer> getAllTrainers() {
        logger.info("Fetching all trainers");
        return trainerRepository.getAllByOrderByIdDesc();
    }

    @Override
    public void updateTrainees(Long id, Set<Trainee> trainees) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");
        if(trainees == null || trainees.isEmpty())
            throw new InvalidDataException("Trainers cannot be null or empty");

        Trainer trainee = trainerRepository.getReferenceById(id);
        trainee.setTrainees(trainees);
        trainerRepository.save(trainee);
    }

    @Override
    public Trainer changePassword(String username, String newPassword) {
        if(username == null || newPassword == null || username.isEmpty() || newPassword.isEmpty())
            throw new InvalidDataException("Username and password are required");

        Trainer trainer = trainerRepository.getByUserUsername(username).orElseThrow(
                IllegalArgumentException::new
        );
        trainer.getUser().setPassword(newPassword);
        return trainerRepository.save(trainer);
    }

    @Override
    public void activateTrainer(Long id) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");

        User user = trainerRepository.getReferenceById(id).getUser();
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void activateTrainer(String username) {
        if(username == null)
            throw new InvalidDataException("Username cannot be null");

        User user = userRepository.getByUsername(username).orElseThrow();
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateTrainer(Long id) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");

        User user = trainerRepository.getReferenceById(id).getUser();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void deactivateTrainer(String username) {
        if(username == null)
            throw new InvalidDataException("Username cannot be null");

        User user = userRepository.getByUsername(username).orElseThrow();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public Set<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername) {
        logger.info("Fetching all trainers not assigned to trainee");
        return new HashSet<>(trainerRepository.getUnassignedTrainersByUserUsername(traineeUsername));
    }

    @Override
    public Set<Trainee> getAllTrainees(String username) {
        if(username.isEmpty())
            throw new InvalidDataException("Username cannot be empty");

        return trainerRepository.getAllTraineesByTraineeUsername(username);
    }
}
