package ua.laboratory.lab_spring_task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> getByUserUsername(String name);
    List<Trainee> getAllByOrderByIdDesc();

    @Query("SELECT t.trainers FROM Trainee t WHERE t.user.username = :username")
    Set<Trainer> getAllTrainersByTraineeUsername(@Param("username") String username);

    void deleteByUserUsername(String name);
}

