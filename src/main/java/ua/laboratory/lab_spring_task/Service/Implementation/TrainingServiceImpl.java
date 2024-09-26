package ua.laboratory.lab_spring_task.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.TrainingType;
import ua.laboratory.lab_spring_task.Service.TrainingService;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private TrainingDAO trainingDAO;

    public TrainingServiceImpl(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Autowired
    public void setTrainingDAO(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Training createOrUpdateTraining(Training training) {
        try {
            if(training == null)
                throw new IllegalArgumentException("Training is null");

            logger.info("Creating training with ID: {}", training.getId());
            return trainingDAO.createOrUpdateTraining(training);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Training getTraining(Long id) {
        try {
            if(id == null)
                throw new IllegalArgumentException("Id cannot be null");

            logger.info("Fetching training with ID: {}", id);
            return trainingDAO.getTrainingById(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public List<Training> getAllTrainings() {
        try {
            logger.info("Fetching all trainings");
            return trainingDAO.getAllTrainings();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public List<Training> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        if(username == null && fromDate == null && toDate == null && trainerName == null && trainingType == null)
            throw new IllegalArgumentException("At least one criteria is required");
        return trainingDAO.getTraineeTrainingsByCriteria(username, fromDate, toDate, trainerName, trainingType);
    }

    @Override
    public List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate, String traineeName, TrainingType trainingType) {
        if(username == null && fromDate == null && toDate == null && traineeName == null && trainingType == null)
            throw new IllegalArgumentException("At least one criteria is required");
        return trainingDAO.getTrainerTrainingsByCriteria(username, fromDate, toDate, traineeName, trainingType);
    }
}
