package ua.laboratory.lab_spring_task.Service.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Service.TrainerService;
import ua.laboratory.lab_spring_task.Util.Utilities;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

    private final TrainerDAO trainerDAO;

    @Autowired
    public TrainerServiceImpl(TrainerDAO trainerDAO) {
        this.trainerDAO = trainerDAO;
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating trainer with ID: {}", trainer.getUserId());
        trainer.setUsername(trainer.getFirstName() + "." + trainer.getLastName());

        if(trainerDAO.getAllTrainers().stream().anyMatch(x -> x.getUsername().equals(trainer.getUsername())))
            trainer.setUsername(trainer.getFirstName() + "." + trainer.getLastName() + trainer.getUserId());

        trainer.setPassword(Utilities.generatePassword(10));

        return trainerDAO.createTrainer(trainer);
    }

    @Override
    public Trainer getTrainer(Long id) {
        logger.info("Fetching trainer with ID: {}", id);
        return trainerDAO.getTrainer(id);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        logger.info("Fetching all trainers");
        return trainerDAO.getAllTrainers();
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Updating trainer with ID: {}", trainer.getUserId());

        return trainerDAO.updateTrainer(trainer);
    }
}
