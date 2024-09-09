package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Util.InMemoryStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAOImpl.class);

    @Autowired
    private Map<Long, Trainer> trainerStorage;

    public TrainerDAOImpl(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        logger.info("Creating trainer with ID: {}", trainer.getUserId());

        trainerStorage.put(trainer.getUserId(), trainer);
        return trainerStorage.get(trainer.getUserId());
    }

    @Override
    public Trainer getTrainer(Long id) {
        logger.debug("Fetching trainer with ID: {}", id);
        return trainerStorage.get(id);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        logger.debug("Fetching all trainers");
        return new ArrayList<>(trainerStorage.values());
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        logger.debug("Updating trainer with ID: {}", trainer.getUserId());
        trainerStorage.put(trainer.getUserId(), trainer);
        return trainerStorage.get(trainer.getUserId());
    }
}
