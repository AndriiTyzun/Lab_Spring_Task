package ua.laboratory.lab_spring_task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.laboratory.lab_spring_task.model.TrainingType;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> getByTrainingTypeName(String name);
}
