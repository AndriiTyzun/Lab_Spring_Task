package ua.laboratory.lab_spring_task.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.laboratory.lab_spring_task.model.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> getAllByOrderByIdDesc();
    @Query("SELECT tr FROM Training tr " +
            "JOIN tr.trainee trainee " +
            "JOIN tr.trainer trainer " +
            "WHERE trainee.user.username = :username " +
            "AND (:fromDate IS NULL OR tr.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR tr.trainingDate <= :toDate) " +
            "AND (:trainerName IS NULL OR trainer.user.username = :trainerName)" +
            "AND (:trainingType IS NULL OR tr.trainingType.trainingTypeName = :trainingType)")
    List<Training> getTraineeTrainingsByCriteria(
            @Param("username") String username,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("trainerName") String trainerName,
            @Param("trainingType") String trainingType
    );

    @Query("SELECT tr FROM Training tr " +
            "JOIN tr.trainer trainer " +
            "JOIN tr.trainee trainee " +
            "WHERE trainer.user.username = :username " +
            "AND (:fromDate IS NULL OR tr.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR tr.trainingDate <= :toDate) " +
            "AND (:traineeName IS NULL OR trainee.user.username = :traineeName)" +
            "AND (:trainingType IS NULL OR tr.trainingType.trainingTypeName = :trainingType)")
    List<Training> getTrainerTrainingsByCriteria(
            @Param("username") String username,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("traineeName") String traineeName,
            @Param("trainingType") String trainingType
    );
}
