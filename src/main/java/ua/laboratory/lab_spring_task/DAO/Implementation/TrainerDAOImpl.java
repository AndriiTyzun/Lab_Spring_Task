package ua.laboratory.lab_spring_task.DAO.Implementation;

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
    @Autowired
    private Map<Long, Trainer> trainerStorage;

    public TrainerDAOImpl(Map<Long, Trainer> trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    @Override
    public Trainer createTrainer(Trainer trainer) {
        trainerStorage.put(trainer.getUserId(), trainer);
        return trainerStorage.get(trainer.getUserId());
    }

    @Override
    public Trainer getTrainer(Long id) {
        return trainerStorage.get(id);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return new ArrayList<>(trainerStorage.values());
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        trainerStorage.put(trainer.getUserId(), trainer);
        return trainerStorage.get(trainer.getUserId());
    }
}
