package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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

import java.time.LocalDate;
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
                    .getTrainingType().getTrainingTypeName(), session);

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

    @Override
    public List<Training> getTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                        String trainerName) {
        Session session = null;
        List<Training> trainings = new ArrayList<>();

        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("SELECT t FROM Training t JOIN t.trainee tr WHERE tr.user.username = :username");

            if(username == null){
                throw new IllegalArgumentException("Username is null");
            }
            if (fromDate != null) {
                hql.append(" AND t.date >= :fromDate");
            }
            if (toDate != null) {
                hql.append(" AND t.date <= :toDate");
            }
            if (trainerName != null && !trainerName.isEmpty()) {
                hql.append(" AND t.trainer.user.username = :trainerName");
            }

            Query<Training> query = session.createQuery(hql.toString(), Training.class);
            query.setParameter("username", username);

            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
            }
            if (trainerName != null && !trainerName.isEmpty()) {
                query.setParameter("trainerName", trainerName);
            }


            trainings = query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch Trainings for Trainee with username: {}", username, e);
            throw new RuntimeException("Failed to fetch Trainings for Trainee with username: " + username, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return trainings;
    }

    @Override
    public List<Training> getTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                        String traineeName) {
        Session session = null;
        List<Training> trainings = new ArrayList<>();

        try {
            session = sessionFactory.openSession();
            StringBuilder hql = new StringBuilder("SELECT t FROM Training t JOIN t.trainer tr WHERE tr.user.username = :username");

            if(username == null){
                throw new IllegalArgumentException("Username is null");
            }
            if (fromDate != null) {
                hql.append(" AND t.date >= :fromDate");
            }
            if (toDate != null) {
                hql.append(" AND t.date <= :toDate");
            }
            if (traineeName != null && !traineeName.isEmpty()) {
                hql.append(" AND t.trainee.user.username = :traineeName");
            }

            Query<Training> query = session.createQuery(hql.toString(), Training.class);
            query.setParameter("username", username);

            if (fromDate != null) {
                query.setParameter("fromDate", fromDate);
            }
            if (toDate != null) {
                query.setParameter("toDate", toDate);
            }
            if (traineeName != null && !traineeName.isEmpty()) {
                query.setParameter("traineeName", traineeName);
            }

            trainings = query.getResultList();
        } catch (Exception e) {
            logger.error("Failed to fetch Trainings for Trainee with username: {}", username, e);
            throw new RuntimeException("Failed to fetch Trainings for Trainee with username: " + username, e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return trainings;
    }


}
