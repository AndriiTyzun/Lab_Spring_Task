package ua.laboratory.lab_spring_task.Util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.Training;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryStorage {
    @Value("${default.data.file.path}")
    private String dataFilePath;

    private Map<Long, Trainer> trainerStorage;
    private Map<Long, Trainee> traineeStorage;
    private Map<Long, Training> trainingStorage;

    public InMemoryStorage() {
        traineeStorage = new HashMap<>();
        trainerStorage = new HashMap<>();
        trainingStorage = new HashMap<>();
    }

    @Bean
    public Map<Long, Trainer> getTrainerStorage() {
        return trainerStorage;
    }

    @Bean
    public Map<Long, Trainee> getTraineeStorage() {
        return traineeStorage;
    }

    @Bean
    public Map<Long, Training> getTrainingStorage() {
        return trainingStorage;
    }

    @PostConstruct
    public void init() throws IOException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(dataFilePath));

            for (String line : lines) {
                String[] parts = line.split(",");
                String entityType = parts[0];

                switch (entityType) {
                    case "trainee":
                        Trainee trainee = new Trainee(
                                parts[2],parts[3],parts[4],parts[5],
                                Boolean.parseBoolean(parts[6]), Long.parseLong(parts[7]),
                                LocalDate.parse(parts[8]),parts[9]

                        );
                        traineeStorage.put(Long.parseLong(parts[7]), trainee);
                        break;
//                    case "trainer":
//                        Trainer trainer = new Trainer(id);
//                        trainerStorage.put(id, trainer);
//                        break;
//                    case "training":
//                        Training training = new Training(id);
//                        trainingStorage.put(id, training);
//                        break;
                    default:
                        throw new IllegalArgumentException("Unknown entity type: " + entityType);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from file", e);
        }
    }
}
