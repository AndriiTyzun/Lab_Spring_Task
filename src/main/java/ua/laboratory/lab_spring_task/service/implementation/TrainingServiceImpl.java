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
import ua.laboratory.lab_spring_task.model.response.TrainingDetailsResponse;
import ua.laboratory.lab_spring_task.service.TrainingService;
import ua.laboratory.lab_spring_task.util.Utilities;
import ua.laboratory.lab_spring_task.util.exceptions.InvalidDataException;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public TrainingDetailsResponse createTraining(String trainingName, LocalDate trainingDate, Long trainingDuration,
                                                  TrainingType trainingType, Trainee trainee, Trainer trainer) {
        if(trainingName == null || trainingDate == null || trainingDuration == null || trainingType == null
                || trainee == null || trainer == null)
            throw new InvalidDataException("Training is null");

        logger.info("Creating training");

        TrainingType type = trainingTypeRepository.getByTrainingTypeName(
                trainingType.getTrainingTypeName()).orElseThrow();

        Training training = new Training(trainingName, trainingDate, trainingDuration, trainingType, trainee, trainer);

        return new TrainingDetailsResponse(trainingRepository.save(training));
    }

    @Override
    public TrainingDetailsResponse updateTraining(Training training) {
        if(training == null)
            throw new InvalidDataException("Training cannot be null");
        training.setTrainingType(trainingTypeRepository.getByTrainingTypeName(
                training.getTrainingType().getTrainingTypeName()).orElseThrow());

        return new TrainingDetailsResponse(trainingRepository.save(training));
    }

    @Override
    public TrainingDetailsResponse getTraining(Long id) {
        if(id == null)
            throw new InvalidDataException("Id cannot be null");

        logger.info("Fetching training with ID: {}", id);
        return new TrainingDetailsResponse(trainingRepository.getReferenceById(id));
    }

    @Override
    public Set<TrainingDetailsResponse> getAllTrainings() {
        logger.info("Fetching all trainings");

        return trainingRepository.getAllByOrderByIdDesc()
                .stream().map(TrainingDetailsResponse::new).collect(Collectors.toSet());
    }

    @Override
    public Set<TrainingType> getAllTrainingTypes() {
        return trainingTypeRepository.getAllByOrderByIdDesc();
    }

    @Override
    public Set<TrainingDetailsResponse> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        if(username == null)
            throw new InvalidDataException("Username is required");

        return trainingRepository.getTraineeTrainingsByCriteria(username, fromDate, toDate, trainerName,trainingType)
                .stream().map(TrainingDetailsResponse::new).collect(Collectors.toSet());
    }

    @Override
    public Set<TrainingDetailsResponse> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String traineeName, String trainingType) {
        if(username == null)
            throw new InvalidDataException("Username is required");

        return trainingRepository.getTrainerTrainingsByCriteria(username, fromDate, toDate, traineeName,trainingType)
                .stream().map(TrainingDetailsResponse::new).collect(Collectors.toSet());
    }
}
