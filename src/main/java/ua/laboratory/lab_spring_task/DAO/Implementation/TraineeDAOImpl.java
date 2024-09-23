package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TraineeDAO;
import ua.laboratory.lab_spring_task.DAO.UserDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TraineeDAOImpl implements TraineeDAO {
    private static final Logger logger = LoggerFactory.getLogger(TraineeDAOImpl.class);
    private final UserDAO userDAO;
    private final SessionFactory sessionFactory;

    public TraineeDAOImpl(UserDAO userDAO, SessionFactory sessionFactory) {
        this.userDAO = userDAO;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Trainee createOrUpdateTrainee(Trainee trainee) {
        Trainee savedTrainee = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            logger.info("Creating trainee with ID: {}", trainee.getId());
            transaction = session.beginTransaction();

            if(trainee.getUser() != null){
                User user = userDAO.createOrUpdateUser(trainee.getUser());
                trainee.setUser(user);
            }
            savedTrainee = session.merge(trainee);

            transaction.commit();
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to create trainee with ID: {}", trainee.getId());
            throw new RuntimeException("Failed to create trainee with ID: " + trainee.getId(), e);
        }
        return savedTrainee;
    }

    @Override
    public Trainee getTraineeById(Long id) {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching trainee with ID: {}", id);
            return session.get(Trainee.class, id);
        }catch (Exception e) {
            logger.error("Failed to fetch trainee with ID: {}", id);
            throw new RuntimeException("Failed to fetch trainee with ID: " + id, e);
        }
    }

    @Override
    public List<Trainee> getAllTrainees() {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching all trainees");
            return session.createQuery("from ua.laboratory.lab_spring_task.Model.Trainee", Trainee.class).list();
        } catch (Exception e){
            logger.error("Failed to fetch all trainees", e);
            throw new RuntimeException("Failed to fetch all trainees", e);
        }
    }

    @Override
    public void deleteTrainee(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(getTraineeById(id));
            transaction.commit();
        } catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to delete trainee with ID: {}", id);
            throw new RuntimeException("Failed to delete trainee with ID: " + id, e);
        }
    }
}
