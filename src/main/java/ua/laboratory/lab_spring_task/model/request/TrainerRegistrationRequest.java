package ua.laboratory.lab_spring_task.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.model.TrainingType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRegistrationRequest {
    private String firstName;
    private String lastName;
    private TrainingType trainingType;
}