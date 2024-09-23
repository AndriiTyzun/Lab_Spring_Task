package ua.laboratory.lab_spring_task.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.Model.Enum.TrainingType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {
    private Long id;
    private TrainingType specialization;
    private User user;
    private List<Trainee> trainees;

//    public Trainer(String firstName, String lastName,
//                   boolean isActive, long id, TrainingType specialization) {
//        super(firstName, lastName, isActive);
//        this.id = id;
//        this.specialization = specialization;
//    }
//
//    public Trainer(String firstName, String lastName, String username, String password,
//                   boolean isActive, Long id, TrainingType specialization) {
//        super(firstName, lastName, username, password, isActive);
//        this.id = id;
//        this.specialization = specialization;
//    }

    public void addTrainee(Trainee trainee) {
        trainees.add(trainee);
    }
}
