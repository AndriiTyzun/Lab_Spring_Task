package ua.laboratory.lab_spring_task.DAOTests;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.*;
import ua.laboratory.lab_spring_task.DAO.Implementation.*;
import ua.laboratory.lab_spring_task.Model.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TrainingDAOTests {
    private static SessionFactory sessionFactory;
    private TrainingDAO trainingDAO;
    private TraineeDAO traineeDAO;
    private TrainerDAO trainerDAO;
    private UserDAO userDAO;
    private TrainingTypeDAO trainingTypeDAO;
    Trainer trainer;
    Trainee trainee;
    TrainingType trainingTypeAg;
    TrainingType trainingTypeSt;
    User user;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);
        trainingTypeDAO = new TrainingTypeDAOImpl(sessionFactory);
        traineeDAO = new TraineeDAOImpl(userDAO,sessionFactory);
        trainerDAO = new TrainerDAOImpl(userDAO,trainingTypeDAO,sessionFactory, traineeDAO);
        trainingDAO = new TrainingDAOImpl(userDAO, trainingTypeDAO, traineeDAO, sessionFactory);

        user = userDAO.createOrUpdateUser(new User("John", "Doe","j.d","1233211231", true));
        trainingTypeAg = trainingTypeDAO.addTrainingType(new TrainingType("Agility"));
        trainingTypeSt = trainingTypeDAO.addTrainingType(new TrainingType("Strength"));
        trainee = traineeDAO.createOrUpdateTrainee(new Trainee(LocalDate.now(), "City, Street, House 1", user));
        trainer = trainerDAO.createOrUpdateTrainer(new Trainer(trainingTypeAg, user));
        Training presavedTrainingAg = trainingDAO.createOrUpdateTraining(new Training("New training", trainingTypeAg,
                LocalDate.now(),2L,trainee,trainer));
        Training presavedTrainingSt = trainingDAO.createOrUpdateTraining(new Training("New training", trainingTypeSt,
                LocalDate.now(),3L,trainee,trainer));
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }


    @Test
    public void testCreateOrUpdateTrainee_AddTrainer() {
        Trainee trainee = new Trainee(LocalDate.now(), "City, Street, House 1",
                new User("John", "Doe","john.doe","1233211231", true));

        Trainee savedTrainee = traineeDAO.createOrUpdateTrainee(trainee);
        assertNotNull(savedTrainee.getId());

        savedTrainee.addTrainer(trainer);
        Trainee updatedTrainee = traineeDAO.createOrUpdateTrainee(savedTrainee);
        Trainer updatedTrainer = trainerDAO.createOrUpdateTrainer(trainer);

        assertNotNull(updatedTrainee);
        assertEquals(savedTrainee.getId(), updatedTrainee.getId());
        assertTrue(updatedTrainee.getTrainers().stream().anyMatch(x -> x.getId().equals(trainer.getId())));
        assertTrue(updatedTrainer.getTrainees().stream().anyMatch(x -> x.getId().equals(updatedTrainee.getId())));
    }

    @Test
    public void testCreateOrUpdateTraining() {
        Training training = new Training("New training", trainingTypeAg,
                LocalDate.now(),2L,trainee,trainer);
        Training savedTraining = trainingDAO.createOrUpdateTraining(training);

        assertNotNull(savedTraining);
        assertNotNull(savedTraining.getId());
        assertEquals(trainer.getId(), savedTraining.getTrainer().getId());
        assertEquals(trainee.getId(), savedTraining.getTrainee().getId());
    }

    @Test
    public void testFindById() {
        Training training = trainingDAO.getTrainingById(1L);

        assertNotNull(training);
        assertNotNull(training.getTrainee());
        assertNotNull(training.getTrainer());
        assertEquals("New training", training.getTrainingName());
    }

    @Test
    public void testGetAllTrainers() {
        List<Training> trainings = trainingDAO.getAllTrainings();

        assertFalse(trainings.isEmpty());
        assertTrue(trainings.stream().anyMatch(t -> t.getId().equals(1L)));
    }

    @Test
    public void testGetTrainingsFiltered() {
        List<Training> trainings = trainingDAO.getTrainerTrainingsByCriteria(
                "j.d",null,null,null,trainingTypeAg);

        assertFalse(trainings.isEmpty());
        assertTrue(trainings.stream().anyMatch(t -> t.getTrainingType().getTrainingTypeName()
                .equals(trainingTypeAg.getTrainingTypeName())));

        trainings = trainingDAO.getTrainerTrainingsByCriteria(
                "j.d",null,null,null,trainingTypeSt);

        assertFalse(trainings.isEmpty());
        assertTrue(trainings.stream().anyMatch(t -> t.getTrainingType().getTrainingTypeName()
                .equals(trainingTypeSt.getTrainingTypeName())));

    }
}
