package ua.laboratory.lab_spring_task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.model.request.UpdateTraineeProfileRequest;
import ua.laboratory.lab_spring_task.model.request.UpdateTrainerProfileRequest;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_type_id")
    private TrainingType specialization;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "trainer_trainee",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "trainee_id")
    )
    @JsonIgnore
    private Set<Trainee> trainees = new HashSet<>();

    public Trainer(TrainingType specialization) {
        this.specialization = specialization;
    }

    public void updateByRequest(UpdateTrainerProfileRequest request){
        this.user.setFirstName(request.getFirstName());
        this.user.setLastName(request.getLastName());
        this.specialization = request.getSpecialization();
        this.user.setActive(request.isActive());
    }

    public void addTrainee(Trainee trainee){
        trainees.add(trainee);
    }
}
