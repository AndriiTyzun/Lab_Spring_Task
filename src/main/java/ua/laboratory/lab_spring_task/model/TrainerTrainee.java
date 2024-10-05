package ua.laboratory.lab_spring_task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainee {
    Long id;
    Trainer trainer;
    Trainee trainee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainerTrainee that = (TrainerTrainee) o;
        return Objects.equals(trainer, that.trainer) && Objects.equals(trainee, that.trainee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainer, trainee);
    }
}
