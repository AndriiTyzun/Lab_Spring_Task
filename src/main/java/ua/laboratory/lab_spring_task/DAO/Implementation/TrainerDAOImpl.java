package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.Model.Trainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAOImpl.class);

    @Autowired
    private Map<Long, Trainer> trainerStorage;

    public TrainerDAOImpl(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        try {
            logger.info("Creating trainer with ID: {}", trainer.getId());

            trainerStorage.put(trainer.getId(), trainer);
            return trainerStorage.get(trainer.getId());
        } catch (Exception e) {
            logger.error("Failed to create trainer with ID: {}", trainer.getId());
            throw new RuntimeException("Failed to create trainer with ID: " + trainer.getId(), e);
        }
    }

    @Override
    public Trainer getTrainer(Long id) {
        try {
            logger.info("Fetching trainer with ID: {}", id);
            return trainerStorage.get(id);
        }catch (Exception e) {
            logger.error("Failed to fetch trainer with ID: {}", id);
            throw new RuntimeException("Failed to fetch trainer with ID: " + id, e);
        }
    }

    @Override
    public List<Trainer> getAllTrainers() {
        try {
            logger.info("Fetching all trainers");
            return new ArrayList<>(trainerStorage.values());
        } catch (Exception e) {
            logger.error("Failed to fetch all trainers", e);
            throw new RuntimeException("Failed to fetch all trainers", e);
        }
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        try {
            logger.info("Updating trainer with ID: {}", trainer.getId());
            trainerStorage.put(trainer.getId(), trainer);
            return trainerStorage.get(trainer.getId());
        }catch (Exception e){
            logger.error("Failed to update trainer with ID: {}", trainer.getId());
            throw new RuntimeException("Failed to update trainer with ID: " + trainer.getId(), e);
        }
    }
}
