package ua.laboratory.lab_spring_task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.dto.TraineeDTO;
import ua.laboratory.lab_spring_task.model.Trainee;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> getByUserUsername(String name);
    List<Trainee> getAllByOrderByIdDesc();
    void deleteByUserUsername(String name);
    @Modifying
    @Query("UPDATE User u SET u.active = :active WHERE u.id = (SELECT t.user.id FROM Trainee t WHERE t.id = :traineeId)")
    void updateUserActiveStatusByTraineeId(@Param("traineeId") Long traineeId, @Param("active") boolean active);
}

