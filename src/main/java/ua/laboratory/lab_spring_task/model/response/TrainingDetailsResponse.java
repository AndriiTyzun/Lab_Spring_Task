package ua.laboratory.lab_spring_task.model.response;

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
public class TrainingDetailsResponse {
    private String name;
    private LocalDate date;
    private TrainingType trainingType;
    private Long duration;
    private String trainerName;

    @Override
    public String toString() {
        return "TrainingDetailsResponse{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", trainingType=" + trainingType +
                ", duration=" + duration +
                ", trainerName='" + trainerName + '\'' +
                '}';
    }
}
