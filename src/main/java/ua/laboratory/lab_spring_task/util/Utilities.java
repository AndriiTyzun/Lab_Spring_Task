package ua.laboratory.lab_spring_task.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.laboratory.lab_spring_task.dao.TraineeRepository;
import ua.laboratory.lab_spring_task.dao.TrainerRepository;
import ua.laboratory.lab_spring_task.dao.UserRepository;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.User;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class Utilities {
    private static UserRepository userRepository;

    @Autowired
    public Utilities(UserRepository userRepository) {
        Utilities.userRepository = userRepository;
    }

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

    public static void setUserUsername(User user){
        user.setUsername(user.getFirstName().toLowerCase() +
                "." + user.getLastName().toLowerCase());

        if(userRepository.getAllByOrderByIdDesc().stream().anyMatch(x -> x
                .getUsername().equals(user.getUsername()))){
            user.setUsername(user.getFirstName().toLowerCase() +
                    "." + user.getLastName().toLowerCase() + user.getId());
        }
    }

    public static Boolean checkCredentials(String username,String password){
        User user = userRepository.getByUsername(username).orElseThrow(
                IllegalArgumentException::new
        );
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
