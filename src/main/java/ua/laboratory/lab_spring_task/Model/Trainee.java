package ua.laboratory.lab_spring_task.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Trainee extends User{
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;

    public Trainee(String firstName, String lastName,
                   boolean isActive, long userId, LocalDate date, String address) {
        super(firstName, lastName, isActive);
        this.userId = userId;
        this.dateOfBirth = date;
        this.address = address;
    }

    public Trainee(String firstName, String lastName, String username, String password,
                   boolean isActive, Long userId, LocalDate dateOfBirth, String address) {
        super(firstName, lastName, username, password, isActive);
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
