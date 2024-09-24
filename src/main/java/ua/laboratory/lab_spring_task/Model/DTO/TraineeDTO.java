package ua.laboratory.lab_spring_task.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.User;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDTO {
    private Long userId;
    private Long traineeId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
    private LocalDate dateOfBirth;
    private String address;

    public TraineeDTO(User user, Trainee trainee) {
        this.userId = user.getId();
        this.traineeId = trainee.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.dateOfBirth = trainee.getDateOfBirth();
        this.address = trainee.getAddress();
    }
}
