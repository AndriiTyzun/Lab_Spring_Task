package ua.laboratory.lab_spring_task.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Service.TrainingService;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainingServiceImpl(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Training createTraining(Training training) {
        logger.info("Creating training with ID: {}", training.getTrainingId());
        return trainingDAO.createTraining(training);
    }

    @Override
    public Training getTraining(Long id) {
        logger.debug("Fetching training with ID: {}", id);
        return trainingDAO.getTraining(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        logger.debug("Fetching all trainings");
        return trainingDAO.getAllTrainings();
    }
}
