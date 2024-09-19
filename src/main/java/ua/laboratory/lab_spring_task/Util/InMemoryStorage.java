package ua.laboratory.lab_spring_task.Util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.Enum.TrainingType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("classpath:application.properties")
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
    public Map<Long, Trainer> trainerStorage() {
        return trainerStorage;
    }

    @Bean
    public Map<Long, Trainee> traineeStorage() {
        return traineeStorage;
    }

    @Bean
    public Map<Long, Training> trainingStorage() {
        return trainingStorage;
    }

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
                        traineeStorage.put(trainee.getTraineeId(), trainee);
                        break;
                    case "trainer":
                        Trainer trainer = new Trainer(
                                parts[2],parts[3],parts[4],parts[5],
                                Boolean.parseBoolean(parts[6]), Long.parseLong(parts[7]),
                                parts[8]
                        );
                        trainerStorage.put(trainer.getTrainerId(), trainer);
                        break;
                    case "training":
                        Training training = new Training(
                                Long.parseLong(parts[1]),Long.parseLong(parts[2]),
                                Long.parseLong(parts[3]),parts[4],
                                TrainingType.valueOf(parts[5]), LocalDate.parse(parts[6]),
                                Long.parseLong(parts[7])
                        );
                        trainingStorage.put(training.getTrainingId(), training);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown entity type: " + entityType);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from file", e);
        }
    }
}
