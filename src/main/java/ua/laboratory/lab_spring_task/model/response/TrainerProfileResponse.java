package ua.laboratory.lab_spring_task.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerProfileResponse {
    private Long trainerId;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private TrainingType trainingType;
    private Set<TraineeProfileResponse> trainees;

    public TrainerProfileResponse(Trainer trainer) {
        this.trainerId = trainer.getId();
        this.username = trainer.getUser().getUsername();
        this.firstName = trainer.getUser().getFirstName();
        this.lastName = trainer.getUser().getLastName();
        this.isActive = trainer.getUser().isActive();
        this.trainingType = trainer.getSpecialization();
        this.trainees = trainer.getTrainees().stream().map(TraineeProfileResponse::new).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "TrainerProfileResponse{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", trainingType=" + trainingType +
                ", trainees=" + trainees +
                '}';
    }
}
