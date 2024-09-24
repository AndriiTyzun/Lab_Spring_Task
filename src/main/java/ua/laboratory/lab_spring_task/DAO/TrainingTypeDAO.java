package ua.laboratory.lab_spring_task.DAO;

import org.hibernate.Session;
import ua.laboratory.lab_spring_task.Model.TrainingType;

public interface TrainingTypeDAO {
    TrainingType getTrainingType(String trainingTypeName);
    TrainingType getTrainingType(String trainingTypeName, Session session);
    TrainingType addTrainingType(TrainingType trainingType);
}
