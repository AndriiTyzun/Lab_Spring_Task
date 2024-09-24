package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.DAO.TrainingDAO;
import ua.laboratory.lab_spring_task.DAO.TrainingTypeDAO;
import ua.laboratory.lab_spring_task.DAO.UserDAO;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.TrainingType;
import ua.laboratory.lab_spring_task.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TrainingDAOImpl implements TrainingDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainingDAOImpl.class);
    private final UserDAO userDAO;
    private final TrainingTypeDAO trainingTypeDAO;
    private final TraineeDAO traineeDAO;
    private final SessionFactory sessionFactory;

    public TrainingDAOImpl(UserDAO userDAO, TrainingTypeDAO trainingTypeDAO, TraineeDAO traineeDAO, SessionFactory sessionFactory) {
        this.userDAO = userDAO;
        this.trainingTypeDAO = trainingTypeDAO;
        this.traineeDAO = traineeDAO;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Training createOrUpdateTraining(Training training) {
        Training savedTraining = null;
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            logger.info("Creating training with ID: {}", training.getId());
            transaction = session.beginTransaction();

            if(training.getTrainee() == null){
                throw new RuntimeException("Trainee is null");
            }

            if(training.getTrainer() == null){
                throw new RuntimeException("Trainer is null");
            }

            TrainingType trainingType = trainingTypeDAO.getTrainingType(training
                    .getTrainingType().getTrainingTypeName());

            if (trainingType == null) {
                throw new RuntimeException("TrainingType is null");
            }
            training.setTrainingType(trainingType);
            savedTraining = session.merge(training);

            transaction.commit();
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to create training with ID: {}", training.getId(),e);
            throw new RuntimeException("Failed to create training with ID: " + training.getId(), e);
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return savedTraining;
    }

    @Override
    public Training getTrainingById(Long id) {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching training with ID: {}", id);
            return session.get(Training.class, id);
        }catch (Exception e) {
            logger.error("Failed to fetch training with ID: {}", id,e);
            throw new RuntimeException("Failed to fetch training with ID: " + id, e);
        }
    }

    @Override
    public List<Training> getAllTrainings() {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching all trainings");
            return session.createQuery("from ua.laboratory.lab_spring_task.Model.Training", Training.class).list();
        } catch (Exception e){
            logger.error("Failed to fetch all trainings", e);
            throw new RuntimeException("Failed to fetch all trainings", e);
        }
    }
}
