package ua.laboratory.lab_spring_task.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.dao.TrainerRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.util.Utilities;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Trainer createOrUpdateTrainer(Trainer trainer) {
        try {
            if(trainer == null)
                throw new IllegalArgumentException("trainer is null");

            logger.info("Creating trainer with ID: {}", trainer.getId());
            Utilities.setUserUsername(trainer.getUser());

            trainer.getUser().setPassword(Utilities.generatePassword(10));

            return trainerRepository.save(trainer);
        }catch (Exception e){
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


        trainerRepository.updateUserActiveStatusByTrainerId(id, true);
    }

    @Override
    public void deactivateTrainer(Long id, Credentials credentials) {
        if(!checkCredentials(credentials))
            throw new IllegalArgumentException("Username and password are required");
        if(id == null)
            throw new IllegalArgumentException("Id cannot be null");

        trainerRepository.updateUserActiveStatusByTrainerId(id, false);
    }

    @Override
    public List<Trainer> getUnassignedTrainersByTraineeUsername(String traineeUsername, Credentials credentials) {
        try {
            if(!checkCredentials(credentials))
                throw new IllegalArgumentException("Username and password are required");

            logger.info("Fetching all trainers not assigned to trainee");
            return trainerRepository.getUnassignedTrainersByUserUsername(traineeUsername);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}
