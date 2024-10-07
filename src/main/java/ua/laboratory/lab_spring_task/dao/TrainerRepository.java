package ua.laboratory.lab_spring_task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import ua.laboratory.lab_spring_task.model.dto.TrainerDTO;
import ua.laboratory.lab_spring_task.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> getByUserUsername(String username);
    List<Trainer> getAllByOrderByIdDesc();
    @Modifying
    @Query("SELECT t FROM Trainer t WHERE t.id NOT IN " +
            "(SELECT tr.id FROM Trainee tr JOIN tr.trainers trainers WHERE tr.user.username = :username)")
    List<Trainer> getUnassignedTrainersByUserUsername(@Param("username") String traineeUsername);

}
