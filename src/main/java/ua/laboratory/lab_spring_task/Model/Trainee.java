package ua.laboratory.lab_spring_task.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {
    private Long id;
    private LocalDate dateOfBirth;
    private String address;
    private User user;
    private Set<Trainer> trainers = new HashSet<>();

    public Trainee(LocalDate date, String address, User user) {
        this.dateOfBirth = date;
        this.address = address;
        this.user = user;
    }

    public void addTrainer(Trainer trainer) {
        trainers.add(trainer);
        trainer.getTrainees().add(this);
    }

    public void removeTrainer(Trainer trainer) {
        trainers.remove(trainer);
        trainer.getTrainees().remove(this);
    }
}
