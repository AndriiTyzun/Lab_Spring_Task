package ua.laboratory.lab_spring_task.Util;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Utilities {
    public static String generatePassword(int length) {
        if(length < 1)
            throw new IllegalArgumentException("Length must be greater than 0");

        byte[] array = new byte[length];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
}
