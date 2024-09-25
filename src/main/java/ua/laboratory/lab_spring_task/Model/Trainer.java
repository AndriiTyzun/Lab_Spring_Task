package ua.laboratory.lab_spring_task.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {
    private Long id;
    private TrainingType specialization;
    private User user;
    private Set<Trainee> trainees = new HashSet<>();

    public Trainer(TrainingType specialization, User user) {
        this.specialization = specialization;
        this.user = user;
    }

    public void addTrainer(Trainee trainee) {
        trainees.add(trainee);
        trainee.getTrainers().add(this);
    }

    public void removeTrainer(Trainee trainee) {
        trainees.remove(trainee);
        trainee.getTrainers().remove(this);
    }
}
