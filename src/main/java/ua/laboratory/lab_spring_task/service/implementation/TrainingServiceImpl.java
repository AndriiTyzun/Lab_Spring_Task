package ua.laboratory.lab_spring_task.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.dao.TrainingRepository;
import ua.laboratory.lab_spring_task.dao.TrainingTypeRepository;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.Training;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.service.TrainingService;
import ua.laboratory.lab_spring_task.util.Utilities;
import ua.laboratory.lab_spring_task.util.exception.InvalidDataException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository, TrainingTypeRepository trainingTypeRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public Training createTraining(String trainingName, LocalDate trainingDate, Long trainingDuration,
                                   TrainingType trainingType, Trainee trainee, Trainer trainer) {
        if(trainingName == null || trainingDate == null || trainingDuration == null || trainingType == null
                || trainee == null || trainer == null)
            throw new InvalidDataException("Training is null");

        logger.info("Creating training");

        TrainingType type = trainingTypeRepository.getByTrainingTypeName(
                trainingType.getTrainingTypeName()).orElse(null);
        if(type == null)
            trainingTypeRepository.save(trainingType);

        Training training = new Training(trainingName, trainingDate, trainingDuration, trainingType, trainee, trainer);

        return trainingRepository.save(training);
    }

    @Override
    public Training updateTraining(Training training) {
        if(training == null)
            throw new InvalidDataException("Training cannot be null");
        trainingTypeRepository.save(training.getTrainingType());
        return trainingRepository.save(training);
    }

    @Override
    public Training getTraining(Long id, Credentials credentials) {
        if(!Utilities.checkCredentials(credentials.getUsername(),credentials.getPassword()))
            throw new InvalidDataException("Username and password are required");
        if(id == null)
            throw new InvalidDataException("Id cannot be null");

        logger.info("Fetching training with ID: {}", id);
        return trainingRepository.getReferenceById(id);
    }

    @Override
    public List<Training> getAllTrainings(Credentials credentials) {
        if(!Utilities.checkCredentials(credentials.getUsername(),credentials.getPassword()))
            throw new InvalidDataException("Username and password are required");
        logger.info("Fetching all trainings");

        return trainingRepository.getAllByOrderByIdDesc();
    }

    @Override
    public Set<TrainingType> getAllTrainingTypes(Credentials credentials) {
        if(!Utilities.checkCredentials(credentials.getUsername(),credentials.getPassword()))
            throw new InvalidDataException("Username and password are required");

        return trainingTypeRepository.getAllByOrderByIdDesc();
    }

    @Override
    public List<Training> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType, Credentials credentials) {
        if(!Utilities.checkCredentials(credentials.getUsername(),credentials.getPassword()))
            throw new InvalidDataException("Username and password are required");
        if(username == null)
            throw new InvalidDataException("Username is required");

        return trainingRepository.getTraineeTrainingsByCriteria(username, fromDate, toDate, trainerName,trainingType);
    }

    @Override
    public List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String traineeName, String trainingType, Credentials credentials) {
        if(!Utilities.checkCredentials(credentials.getUsername(),credentials.getPassword()))
            throw new InvalidDataException("Username and password are required");
        if(username == null)
            throw new InvalidDataException("Username is required");

        return trainingRepository.getTrainerTrainingsByCriteria(username, fromDate, toDate, traineeName,trainingType);
    }
}
