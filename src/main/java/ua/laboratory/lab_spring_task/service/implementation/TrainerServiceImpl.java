package ua.laboratory.lab_spring_task.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.dao.TrainerRepository;
import ua.laboratory.lab_spring_task.dao.TrainingTypeRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.util.Utilities;

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
        try {
            if(firstName == null || lastName == null || trainingType == null)
                throw new IllegalArgumentException("Trainer cannot be null");

            logger.info("Creating trainer");
            logger.info("Creating trainee");

            TrainingType type = trainingTypeRepository.getByTrainingTypeName(
                    trainingType.getTrainingTypeName()).orElse(null);
            if(type == null)
                trainingTypeRepository.save(trainingType);


            User user = new User(firstName, lastName);
            Utilities.setUserUsername(user);
            user.setPassword(Utilities.generatePassword(10));
            user.setActive(true);

            Trainer trainer = new Trainer(type);
            trainer.setUser(user);

            return trainerRepository.save(trainer);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Trainer updateTrainer(Trainer trainer, Credentials credentials) {
        if(credentials.getUsername() == null || credentials.getPassword() == null ||
                credentials.getUsername().isEmpty() || credentials.getPassword().isEmpty())
            throw new IllegalArgumentException("Username and password are required");
        if(trainer == null)
            throw new IllegalArgumentException("Trainee cannot be null");

        userRepository.save(trainer.getUser());
        return trainerRepository.save(trainer);
    }

    @Override
    public Boolean checkCredentials(Credentials credentials) {
        if(credentials.getUsername() == null || credentials.getPassword() == null ||
                credentials.getUsername().isEmpty() || credentials.getPassword().isEmpty())
            throw new IllegalArgumentException("Username and password are required");

        return Utilities.checkCredentials(credentials.getUsername(), credentials.getPassword());
    }

    @Override
    public Trainer getTrainerById(Long id, Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            if(id == null)
                throw new IllegalArgumentException("Id cannot be null");

            logger.info("Fetching trainer with ID: {}", id);
            return trainerRepository.getReferenceById(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Trainer getTrainerByUsername(String username, Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            if(username == null)
                throw new IllegalArgumentException("Username cannot be null");

            logger.info("Fetching trainer with username: {}", username);
            return trainerRepository.getByUserUsername(username).orElseThrow(
                    IllegalArgumentException::new
            );
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public List<Trainer> getAllTrainers(Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");
            logger.info("Fetching all trainers");
            return trainerRepository.getAllByOrderByIdDesc();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public void updateTrainees(Long id, Set<Trainee> trainees, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");
        if(trainees == null || trainees.isEmpty())
            throw new IllegalArgumentException("Trainers cannot be null or empty");

        Trainer trainee = trainerRepository.getReferenceById(id);
        trainee.setTrainees(trainees);
        trainerRepository.save(trainee);
    }

    @Override
    public Trainer changePassword(String username, String newPassword, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(username == null || newPassword == null || username.isEmpty() || newPassword.isEmpty())
            throw new IllegalArgumentException("Username and password are required");


        Trainer trainer = trainerRepository.getByUserUsername(username).orElseThrow(
                IllegalArgumentException::new
        );
        trainer.getUser().setPassword(newPassword);
        return trainerRepository.save(trainer);
    }

    @Override
    public void activateTrainer(Long id, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");

        User user = trainerRepository.getReferenceById(id).getUser();
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void activateTrainer(String username, Credentials credentials) {
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
    public void deactivateTrainer(Long id, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");

        User user = trainerRepository.getReferenceById(id).getUser();
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void deactivateTrainer(String username, Credentials credentials) {
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
    public Set<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername, Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");

            logger.info("Fetching all trainers not assigned to trainee");
            return new HashSet<>(trainerRepository.getUnassignedTrainersByUserUsername(traineeUsername));
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Set<Trainee> getAllTrainees(String username, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(username.isEmpty())
            throw new IllegalArgumentException("Username cannot be empty");

        return trainerRepository.getAllTraineesByTraineeUsername(username);
    }
}
