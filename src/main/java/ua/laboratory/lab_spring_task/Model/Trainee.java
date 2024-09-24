package ua.laboratory.lab_spring_task.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {
    private Long id;
    private LocalDate dateOfBirth;
    private String address;
    private User user;
    public List<Trainer> trainers;

    public Trainee(LocalDate date, String address, User user) {
        this.dateOfBirth = date;
        this.address = address;
        this.user = user;
    }
}
