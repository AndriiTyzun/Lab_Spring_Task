package ua.laboratory.lab_spring_task.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Service.TraineeService;
import ua.laboratory.lab_spring_task.Util.Utilities;

import java.util.List;

//@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private TraineeDAO traineeDAO;

    public TraineeServiceImpl(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Autowired
    public void setTraineeDAO(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Override
    public Trainee createOrUpdateTrainee(Trainee trainee) {
        try {
            logger.info("Creating trainee with ID: {}", trainee.getId());
            trainee.setUsername(trainee.getFirstName() + "." + trainee.getLastName());

            if(traineeDAO.getAllTrainees().stream().anyMatch(x -> x.getUsername().equals(trainee.getUsername())))
                trainee.setUsername(trainee.getFirstName() + "." + trainee.getLastName() + trainee.getId());

            trainee.setPassword(Utilities.generatePassword(10));

            return traineeDAO.createOrUpdateTrainee(trainee);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Trainee getTraineeById(Long id) {
        try {
            logger.info("Fetching trainee with ID: {}", id);
            return traineeDAO.getTraineeById(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }

    }

    @Override
    public List<Trainee> getAllTrainees() {
        try {
            logger.info("Fetching all trainees");
            return traineeDAO.getAllTrainees();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public void deleteTrainee(Long id) {
        try {
            logger.info("Deleting trainee with ID: {}", id);
            traineeDAO.deleteTrainee(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}

