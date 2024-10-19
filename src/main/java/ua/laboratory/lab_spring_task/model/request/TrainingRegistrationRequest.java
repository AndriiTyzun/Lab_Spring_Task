package ua.laboratory.lab_spring_task.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.model.TrainingType;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRegistrationRequest {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private LocalDate trainingDate;
    private Long trainingDuration;
    private TrainingType trainingType;

    @Override
    public String toString() {
        return "TrainingRegistrationRequest{" +
                "traineeUsername='" + traineeUsername + '\'' +
                ", trainerUsername='" + trainerUsername + '\'' +
                ", trainingName='" + trainingName + '\'' +
                ", trainingDate=" + trainingDate +
                ", trainingDuration=" + trainingDuration +
                ", trainingType=" + trainingType +
                '}';
    }
}
