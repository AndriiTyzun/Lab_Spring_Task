package ua.laboratory.lab_spring_task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.model.request.UpdateTraineeProfileRequest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainees")
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column
    private LocalDate dateOfBirth;
    @Column
    private String address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(mappedBy = "trainees", fetch = FetchType.EAGER)
    private Set<Trainer> trainers = new HashSet<>();

    public Trainee(LocalDate dateOfBirth, String address) {
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public void updateByRequest(UpdateTraineeProfileRequest request){
        this.user.setFirstName(request.getFirstName());
        this.user.setLastName(request.getLastName());
        this.dateOfBirth = request.getDateOfBirth();
        this.address = request.getAddress();
        this.user.setActive(request.isActive());
    }

}
