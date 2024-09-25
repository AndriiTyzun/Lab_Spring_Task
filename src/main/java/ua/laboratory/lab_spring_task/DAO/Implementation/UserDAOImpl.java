package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.UserDAO;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.TrainingType;
import ua.laboratory.lab_spring_task.Model.User;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    private final SessionFactory sessionFactory;

    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User createOrUpdateUser(User user) {
        User savedUser = null;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()){
            logger.info("Creating user with ID: {}", user.getId());

            transaction = session.beginTransaction();
            savedUser = session.merge(user);
            transaction.commit();
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to create user with ID: {}", user.getId());
            throw new RuntimeException("Failed to create user with ID: " + user.getId(), e);
        }
        return savedUser;
    }

    @Override
    public User getUserById(Long id) {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching user with ID: {}", id);
            return session.get(User.class, id);
        }catch (Exception e) {
            logger.error("Failed to fetch user with ID: {}", id);
            throw new RuntimeException("Failed to fetch user with ID: " + id, e);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching user with Username: {}", username);

            Query<User> query = session.createQuery(
                    "FROM User WHERE username = :name", User.class);
            query.setParameter("name", username);
            return query.uniqueResultOptional().orElse(null);
        }catch (Exception e) {
            logger.error("Failed to fetch user", e);
            throw new RuntimeException("Failed to fetch user ", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching all users");
            return session.createQuery("from ua.laboratory.lab_spring_task.Model.User", User.class).list();
        } catch (Exception e){
            logger.error("Failed to fetch all users", e);
            throw new RuntimeException("Failed to fetch all users", e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(getUserById(id));
            transaction.commit();
        } catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to delete user with ID: {}", id);
            throw new RuntimeException("Failed to delete user with ID: " + id, e);
        }
    }
}
