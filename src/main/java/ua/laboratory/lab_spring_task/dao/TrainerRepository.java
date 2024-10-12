package ua.laboratory.lab_spring_task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> getByUserUsername(String username);
    List<Trainer> getAllByOrderByIdDesc();
    @Modifying
    @Query("SELECT t FROM Trainer t WHERE t.id NOT IN " +
            "(SELECT tr.id FROM Trainee tr JOIN tr.trainers trainers WHERE tr.user.username = :username)")
    List<Trainer> getUnassignedTrainersByUserUsername(@Param("username") String traineeUsername);

    @Query("SELECT t.trainers FROM Trainee t WHERE t.user.username = :username")
    Set<Trainee> getAllTraineesByTraineeUsername(@Param("username") String username);
}
