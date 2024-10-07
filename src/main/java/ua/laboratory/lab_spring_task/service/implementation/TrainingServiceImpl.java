package ua.laboratory.lab_spring_task.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.dao.TrainingRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.Training;
import ua.laboratory.lab_spring_task.service.TrainingService;
import ua.laboratory.lab_spring_task.util.Utilities;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private final TrainingRepository trainingRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Override
    public Training createTraining(String trainingName, LocalDate trainingDate, Long trainingDuration,
                                   TrainingType trainingType, Trainee trainee, Trainer trainer) {
        try {
            if(trainingName == null || trainingDate == null || trainingDuration == null || trainingType == null
                    || trainee == null || trainer == null)
                throw new IllegalArgumentException("Training is null");
            logger.info("Creating training");

            Training training = new Training(trainingName, trainingDate, trainingDuration, trainingType, trainee, trainer);

            return trainingRepository.save(training);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Training updateTraining(Training training) {
        if(training == null)
            throw new IllegalArgumentException("Training cannot be null");

        return trainingRepository.save(training);
    }

    @Override
    public Training getTraining(Long id, Credentials credentials) {
        try {
            if(!Utilities.checkCredentials(credentials.getUsername(),credentials.getPassword()))
                throw new IllegalArgumentException("Username and password are required");
            if(id == null)
                throw new IllegalArgumentException("Id cannot be null");

            logger.info("Fetching training with ID: {}", id);
            return trainingRepository.getReferenceById(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public List<Training> getAllTrainings(Credentials credentials) {
        try {
            if(!Utilities.checkCredentials(credentials.getUsername(),credentials.getPassword()))
                throw new IllegalArgumentException("Username and password are required");
            logger.info("Fetching all trainings");
            return trainingRepository.getAllByOrderByIdDesc();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public List<Training> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerName, Credentials credentials) {
        if(!Utilities.checkCredentials(credentials.getUsername(),credentials.getPassword()))
            throw new IllegalArgumentException("Username and password are required");
        if(username == null)
            throw new IllegalArgumentException("Username is required");
        return trainingRepository.getTraineeTrainingsByCriteria(username, fromDate, toDate, trainerName);
    }

    @Override
    public List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String traineeName, Credentials credentials) {
        if(!Utilities.checkCredentials(credentials.getUsername(),credentials.getPassword()))
            throw new IllegalArgumentException("Username and password are required");
        if(username == null)
            throw new IllegalArgumentException("Username is required");
        return trainingRepository.getTrainerTrainingsByCriteria(username, fromDate, toDate, traineeName);
    }
}
