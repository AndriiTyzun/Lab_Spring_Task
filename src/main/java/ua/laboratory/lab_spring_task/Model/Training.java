package ua.laboratory.lab_spring_task.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.Model.Enum.TrainingType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Training {
    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Long trainingDuration;
    private Trainee trainee;
    private Trainer trainer;

    public Training(Long trainingId, Long traineeId, Long trainerId, String trainingName,
                    TrainingType trainingType, LocalDate trainingDate, Long trainingDuration) {
        this.id = trainingId;
        this.traineeId = traineeId;
        this.trainerId = trainerId;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
    }
}
