package ua.laboratory.lab_spring_task.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Trainee extends User{
    private Long id;
    private LocalDate dateOfBirth;
    private String address;
    private User user;
    public List<Trainer> trainers;

    public Trainee(String firstName, String lastName,
                   boolean isActive, long traineeId, LocalDate date, String address) {
        super(firstName, lastName, isActive);
        this.id = traineeId;
        this.dateOfBirth = date;
        this.address = address;
    }

    public Trainee(String firstName, String lastName, String username, String password,
                   boolean isActive, Long id, LocalDate dateOfBirth, String address) {
        super(firstName, lastName, username, password, isActive);
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
