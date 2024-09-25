package ua.laboratory.lab_spring_task.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
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

    public void setTrainerDAO(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Override
    public Trainer createOrUpdateTrainer(Trainer trainer) {
        try {
            logger.info("Creating trainer with ID: {}", trainer.getId());
            Utilities.setTrainerUsername(trainer, trainerDAO);

            trainer.getUser().setPassword(Utilities.generatePassword(10));

            return trainerDAO.createOrUpdateTrainer(trainer);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Boolean checkCredentials(String username, String password) {
        if(username == null || password == null) {
            throw new IllegalArgumentException("Username and password are required");
        }
        if(username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username and password are required");
        }
        return Utilities.checkCredentials(username, password, trainerDAO);
    }

    @Override
    public Trainer getTrainerById(Long id) {
        try {
            logger.info("Fetching trainer with ID: {}", id);
            return trainerDAO.getTrainerById(id);
        } catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public Trainer getTrainerByUsername(String username) {
        try {
            logger.info("Fetching trainer with username: {}", username);
            return trainerDAO.getTrainerByUsername(username);
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
    public Trainer changePassword(String username, String newPassword) {
        Trainer trainer = trainerDAO.getTrainerByUsername(username);
        trainer.getUser().setPassword(newPassword);
        return trainerDAO.createOrUpdateTrainer(trainer);
    }

    @Override
    public Trainer activateTrainer(Long id) {
        Trainer trainer = trainerDAO.getTrainerById(id);
        trainer.getUser().setActive(true);
        return trainerDAO.createOrUpdateTrainer(trainer);
    }

    @Override
    public Trainer deactivateTrainer(Long id) {
        Trainer trainer = trainerDAO.getTrainerById(id);
        trainer.getUser().setActive(false);
        return trainerDAO.createOrUpdateTrainer(trainer);
    }

}
