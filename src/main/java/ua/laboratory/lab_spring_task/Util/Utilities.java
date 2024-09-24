package ua.laboratory.lab_spring_task.Util;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ua.laboratory.lab_spring_task.Model.TrainingType;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

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
}
