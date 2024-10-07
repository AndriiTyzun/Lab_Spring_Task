package ua.laboratory.lab_spring_task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String trainingName;
    @Column(nullable = false)
    private LocalDate trainingDate;
    @Column(nullable = false)
    private Long trainingDuration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    public Training(String trainingName, LocalDate trainingDate, Long trainingDuration, TrainingType trainingType, Trainee trainee, Trainer trainer) {
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.trainingType = trainingType;
        this.trainee = trainee;
        this.trainer = trainer;
    }
}
