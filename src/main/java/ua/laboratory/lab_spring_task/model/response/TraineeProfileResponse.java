package ua.laboratory.lab_spring_task.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.request.UpdateTraineeProfileRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeProfileResponse {
    private Long traineeId;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private LocalDate dateOfBirth;
    private String address;
    private Set<TrainerProfileResponse> trainers;

    public TraineeProfileResponse(Trainee trainee) {
        this.traineeId = trainee.getId();
        this.username = trainee.getUser().getUsername();
        this.firstName = trainee.getUser().getFirstName();
        this.lastName = trainee.getUser().getLastName();
        this.isActive = trainee.getUser().isActive();
        this.dateOfBirth = trainee.getDateOfBirth();
        this.address = trainee.getAddress();
        this.trainers = trainee.getTrainers().stream().map(TrainerProfileResponse::new).collect(Collectors.toSet());
    }



    @Override
    public String toString() {
        return "TraineeProfileResponse{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", trainers=" + trainers +
                '}';
    }
}
