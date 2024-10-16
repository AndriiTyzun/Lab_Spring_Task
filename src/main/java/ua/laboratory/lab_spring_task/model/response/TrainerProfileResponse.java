package ua.laboratory.lab_spring_task.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.model.TrainingType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileResponse {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private TrainingType trainingType;
    private List<TraineeProfileResponse> trainees;

    @Override
    public String toString() {
        return "username = " + username + ", " +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", " +
                "active = " + isActive + ", " +
                "training type = " + trainingType;
    }
}
