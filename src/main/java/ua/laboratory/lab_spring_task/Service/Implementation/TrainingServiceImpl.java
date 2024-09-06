package ua.laboratory.lab_spring_task.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Service.TrainingService;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDAO trainingDAO;

    @Autowired
    public TrainingServiceImpl(TrainingDAO trainingDAO) {
        this.trainingDAO = trainingDAO;
    }

    @Override
    public Training createTraining(Training training) {
        return trainingDAO.createTraining(training);
    }

    @Override
    public Training getTraining(Long id) {
        return trainingDAO.getTraining(id);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingDAO.getAllTrainings();
    }
}
