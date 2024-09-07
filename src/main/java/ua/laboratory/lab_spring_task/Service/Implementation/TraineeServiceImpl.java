package ua.laboratory.lab_spring_task.Service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Service.TraineeService;
import ua.laboratory.lab_spring_task.Util.Utilities;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDAO traineeDAO;

    @Autowired
    public TraineeServiceImpl(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Override
    public Trainee createTrainee(Trainee trainee) {
        trainee.setUsername(trainee.getFirstName() + "." + trainee.getLastName());

        if(traineeDAO.getAllTrainees().stream()
                .anyMatch(x -> x.getUsername().equals(trainee.getUsername())))
            trainee.setUsername(trainee.getFirstName() + "." + trainee.getLastName() +
                    trainee.getUserId());

        trainee.setPassword(Utilities.generatePassword(10));


        return traineeDAO.createTrainee(trainee);
    }

    @Override
    public Trainee getTraineeById(Long id) {
        return traineeDAO.getTraineeById(id);
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeDAO.getAllTrainees();
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeDAO.updateTrainee(trainee);
    }

    @Override
    public void deleteTrainee(Long id) {
        traineeDAO.deleteTrainee(id);
    }
}
