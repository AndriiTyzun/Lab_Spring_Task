package ua.laboratory.lab_spring_task.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {
    private Long id;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Long trainingDuration;
    private Trainee trainee;
    private Trainer trainer;

    public Training(String trainingName, TrainingType trainingType, LocalDate trainingDate,
                    Long trainingDuration, Trainee trainee, Trainer trainer) {
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.trainee = trainee;
        this.trainer = trainer;
    }
}
