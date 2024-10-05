package ua.laboratory.lab_spring_task.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.User;

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


    public TrainerDTO(User user, Trainer trainer) {
        this.userId = user.getId();
        this.trainerId = trainer.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.specialization = trainer.getSpecialization();
    }
}
