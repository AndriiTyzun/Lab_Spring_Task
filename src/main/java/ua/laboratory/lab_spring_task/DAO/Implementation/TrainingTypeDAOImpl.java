package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TrainingTypeDAO;
import ua.laboratory.lab_spring_task.DAO.UserDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.TrainingType;
import ua.laboratory.lab_spring_task.Model.User;

@Repository
public class TrainingTypeDAOImpl implements TrainingTypeDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeDAOImpl.class);
    private final SessionFactory sessionFactory;

    public TrainingTypeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public TrainingType getTrainingType(String trainingTypeName) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<TrainingType> query = session.createQuery(
                    "FROM TrainingType WHERE trainingTypeName = :name", TrainingType.class);
            query.setParameter("name", trainingTypeName);
            return query.uniqueResultOptional().orElse(null);
        }catch (Exception e) {
            logger.error("Failed to fetch TrainingType ",e);
            throw new RuntimeException("Failed to fetch TrainingType ", e);
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public TrainingType getTrainingType(String trainingTypeName, Session session) {
        try {
            Query<TrainingType> query = session.createQuery(
                    "FROM TrainingType WHERE trainingTypeName = :name", TrainingType.class);
            query.setParameter("name", trainingTypeName);
            return query.uniqueResultOptional().orElse(null);
        }catch (Exception e) {
            logger.error("Failed to fetch TrainingType ",e);
            throw new RuntimeException("Failed to fetch TrainingType ", e);
        }
    }

    @Override
    public TrainingType addTrainingType(TrainingType trainingType) {
        Transaction transaction = null;
        Session session = null;
        TrainingType savedTrainingType = null;
        try {
            session = sessionFactory.openSession();
            logger.info("Creating trainingType");
            transaction = session.beginTransaction();

            savedTrainingType = session.merge(trainingType);

            transaction.commit();
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to create trainingType",e);
            throw new RuntimeException("Failed to create trainingType ", e);
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return savedTrainingType;
    }
}
