package ua.laboratory.lab_spring_task.util;

import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.DAO.UserDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.User;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Utilities {

    public static String generatePassword(int length) throws NoSuchAlgorithmException {
        if(length < 1)
            throw new IllegalArgumentException("Length must be greater than 0");
        String characters = "0123456789abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();

        return secureRandom
                .ints(length, 0, characters.length())
                .mapToObj(characters::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static Trainee setTraineeUsername(Trainee trainee, TraineeDAO traineeDAO){
        trainee.getUser().setUsername(trainee.getUser().getFirstName() +
                "." + trainee.getUser().getLastName());

        if(traineeDAO.getAllTrainees().stream().anyMatch(x -> x.getUser()
                .getUsername().equals(trainee.getUser().getUsername()))){
            trainee.getUser().setUsername(trainee.getUser().getFirstName() +
                    "." + trainee.getUser().getLastName() + trainee.getId());
        }
        return trainee;
    }

    public static Trainer setTrainerUsername(Trainer trainer, TrainerDAO trainerDAO){
        trainer.getUser().setUsername(trainer.getUser().getFirstName() +
                "." + trainer.getUser().getLastName());

        if(trainerDAO.getAllTrainers().stream().anyMatch(x -> x.getUser()
                .getUsername().equals(trainer.getUser().getUsername()))){
            trainer.getUser().setUsername(trainer.getUser().getFirstName() +
                    "." + trainer.getUser().getLastName() + trainer.getId());
        }
        return trainer;
    }

    public static Boolean checkCredentials(String username,String password, TraineeDAO traineeDAO){
        Trainee existingTrainee = traineeDAO.getTraineeByUsername(username);
        if(existingTrainee == null){
            return false;
        }
        if(!existingTrainee.getUser().getPassword()
                .equals(password)){
            return false;
        }
        return true;
    }

    public static Boolean checkCredentials(String username,String password, TrainerDAO trainerDAO){
        Trainer existingTrainer = trainerDAO.getTrainerByUsername(username);
        if(existingTrainer == null){
            return false;
        }
        if(!existingTrainer.getUser().getPassword()
                .equals(password)){
            return false;
        }
        return true;
    }

    public static Boolean checkCredentials(String username,String password, UserDAO userDAO){
        User user = userDAO.getUserByUsername(username);
        if(user == null){
            return false;
        }
        if(!user.getPassword()
                .equals(password)){
            return false;
        }
        return true;
    }
}
