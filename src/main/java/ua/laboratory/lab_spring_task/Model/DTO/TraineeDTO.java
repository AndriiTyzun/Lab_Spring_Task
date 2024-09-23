package ua.laboratory.lab_spring_task.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;

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
}
