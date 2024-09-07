package ua.laboratory.lab_spring_task.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainer extends User{
    private Long userId;
    private String specialization;

    public Trainer(String firstName, String lastName,
                   boolean isActive, long userId, String specialization) {
        super(firstName, lastName, isActive);
        this.userId = userId;
        this.specialization = specialization;
    }

    public Trainer(String firstName, String lastName, String username, String password,
                   boolean isActive, Long userId, String specialization) {
        super(firstName, lastName, username, password, isActive);
        this.userId = userId;
        this.specialization = specialization;
    }
}
