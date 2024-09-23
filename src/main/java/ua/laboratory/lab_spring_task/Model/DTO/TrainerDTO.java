package ua.laboratory.lab_spring_task.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.Model.Enum.TrainingType;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDTO {
    private Long userId;
    private Long trainerId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
    private TrainingType specialization;
}
