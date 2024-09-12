package ua.laboratory.lab_spring_task.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Service.TrainerService;
import ua.laboratory.lab_spring_task.Util.Utilities;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private TrainerDAO trainerDAO;

    public TrainerServiceImpl(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Autowired
    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Override
    public Trainer createTrainer(Trainer trainer) throws NoSuchAlgorithmException {
        try {
            logger.info("Creating trainer with ID: {}", trainer.getUserId());
            trainer.setUsername(trainer.getFirstName() + "." + trainer.getLastName());

            if (trainerDAO.getAllTrainers().stream().anyMatch(x -> x.getUsername().equals(trainer.getUsername())))
                trainer.setUsername(trainer.getFirstName() + "." + trainer.getLastName() + trainer.getUserId());

            trainer.setPassword(Utilities.generatePassword(10));

            return trainerDAO.createTrainer(trainer);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Trainer getTrainer(Long id) {
        try {
            logger.info("Fetching trainer with ID: {}", id);
            return trainerDAO.getTrainer(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public List<Trainer> getAllTrainers() {
        try {
            logger.info("Fetching all trainers");
            return trainerDAO.getAllTrainers();
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        try {
            logger.info("Updating trainer with ID: {}", trainer.getUserId());

            return trainerDAO.updateTrainer(trainer);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}
