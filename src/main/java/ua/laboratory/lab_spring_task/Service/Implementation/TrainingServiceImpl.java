package ua.laboratory.lab_spring_task.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Service.TrainingService;

import java.util.List;

//@Service
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
    public Training createTraining(Training training) {
        try {
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
}
