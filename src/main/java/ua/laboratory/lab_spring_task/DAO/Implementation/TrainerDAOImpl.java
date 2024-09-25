package ua.laboratory.lab_spring_task.DAO.Implementation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.laboratory.lab_spring_task.DAO.TrainerDAO;
import ua.laboratory.lab_spring_task.DAO.TrainingTypeDAO;
import ua.laboratory.lab_spring_task.DAO.UserDAO;
import ua.laboratory.lab_spring_task.Model.*;
import ua.laboratory.lab_spring_task.Model.DTO.TrainerDTO;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private static final Logger logger = LoggerFactory.getLogger(TrainerDAOImpl.class);
    private final UserDAO userDAO;
    private final TrainingTypeDAO trainingTypeDAO;
    private final SessionFactory sessionFactory;

    public TrainerDAOImpl(UserDAO userDAO, TrainingTypeDAO trainingTypeDAO, SessionFactory sessionFactory) {
        this.userDAO = userDAO;
        this.trainingTypeDAO = trainingTypeDAO;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Trainer createOrUpdateTrainer(Trainer trainer) {
        Trainer savedTrainer = null;
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            logger.info("Creating trainer with ID: {}", trainer.getId());
            transaction = session.beginTransaction();

            if(trainer.getUser() != null){
                User user = userDAO.createOrUpdateUser(trainer.getUser());
                trainer.setUser(user);
            }

            TrainingType trainingType = trainingTypeDAO.getTrainingType(trainer
                    .getSpecialization().getTrainingTypeName(), session);

            if (trainingType == null) {
                throw new RuntimeException("Invalid Training Type: " + trainer.getSpecialization().getTrainingTypeName());
            }
            trainer.setSpecialization(trainingType);
            savedTrainer = session.merge(trainer);

            transaction.commit();
        }catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to create trainer with ID: {}", trainer.getId(),e);
            throw new RuntimeException("Failed to create trainer with ID: " + trainer.getId(), e);
        }finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return savedTrainer;
    }

    @Override
    public Trainer getTrainerById(Long id) {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching trainer with ID: {}", id);
            return session.get(Trainer.class, id);
        }catch (Exception e) {
            logger.error("Failed to fetch trainer with ID: {}", id,e);
            throw new RuntimeException("Failed to fetch trainer with ID: " + id, e);
        }
    }

    @Override
    public Trainer getTrainerByUsername(String username) {
        return userDAO.getUserByUsername(username).getTrainer();
    }

    @Override
    public TrainerDTO getTrainerDTOById(Long id) {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching trainer DTO with ID: {}", id);
            Trainer trainer = session.get(Trainer.class, id);
            User user = userDAO.getUserById(trainer.getUser().getId());
            return new TrainerDTO(user, trainer);
        }catch (Exception e) {
            logger.error("Failed to fetch trainer with ID: {}, ", id,e);
            throw new RuntimeException("Failed to fetch trainer with ID: " + id, e);
        }
    }


    @Override
    public List<Trainer> getAllTrainers() {
        try (Session session = sessionFactory.openSession()){
            logger.info("Fetching all trainers");
            return session.createQuery("from ua.laboratory.lab_spring_task.Model.Trainer", Trainer.class).list();
        } catch (Exception e){
            logger.error("Failed to fetch all trainers", e);
            throw new RuntimeException("Failed to fetch all trainers", e);
        }
    }
}
