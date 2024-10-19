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
public class UpdateTrainerProfileRequest {
    private String firstName;
    private String lastName;
    private TrainingType specialization;
    private boolean isActive;

    @Override
    public String toString() {
        return "UpdateTrainerProfileRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialization=" + specialization +
                ", isActive=" + isActive +
                '}';
    }
}
