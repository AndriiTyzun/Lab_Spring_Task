package ua.laboratory.lab_spring_task.ServiceTests;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.DAO.*;
import ua.laboratory.lab_spring_task.DAO.Implementation.*;
import ua.laboratory.lab_spring_task.Model.*;
import ua.laboratory.lab_spring_task.Service.Implementation.TraineeServiceImpl;
import ua.laboratory.lab_spring_task.Service.Implementation.TrainerServiceImpl;
import ua.laboratory.lab_spring_task.Service.Implementation.TrainingServiceImpl;
import ua.laboratory.lab_spring_task.Service.TrainingService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingServiceTests {
    private TraineeServiceImpl traineeService;
    private TrainerServiceImpl trainerService;
    private TrainingServiceImpl trainingService;
    private static SessionFactory sessionFactory;
    User presavedUser;
    Trainee presavedTrainee;
    Trainer presavedTrainer;
    TrainingType trainingTypeAg;
    TrainingType trainingTypeSt;
    Training presavedTrainingAg;
    Training presavedTrainingSt;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        UserDAO userDAO = new UserDAOImpl(sessionFactory);
        TrainingTypeDAO trainingTypeDAO = new TrainingTypeDAOImpl(sessionFactory);
        TraineeDAO traineeDAO = new TraineeDAOImpl(userDAO,sessionFactory);
        TrainerDAO trainerDAO = new TrainerDAOImpl(userDAO, trainingTypeDAO, sessionFactory, traineeDAO);
        TrainingDAO trainingDAO = new TrainingDAOImpl(userDAO, trainingTypeDAO, traineeDAO, sessionFactory);
        traineeService = new TraineeServiceImpl(traineeDAO);
        trainerService = new TrainerServiceImpl(trainerDAO);
        trainingService = new TrainingServiceImpl(trainingDAO);

        trainingTypeAg = trainingTypeDAO.addTrainingType( new TrainingType("Agility"));
        trainingTypeSt = trainingTypeDAO.addTrainingType( new TrainingType("Strength"));

        presavedUser = userDAO.createOrUpdateUser(new User("John", "Doe","j.d","1233211231", true));
        presavedTrainee = traineeService.createOrUpdateTrainee(new Trainee(LocalDate.now(), "City, Street, House 1", presavedUser));
        presavedTrainer = trainerDAO.createOrUpdateTrainer(new Trainer(trainingTypeAg, presavedUser));

        presavedTrainingAg = trainingDAO.createOrUpdateTraining(new Training("New training", trainingTypeAg,
                LocalDate.now(),2L,presavedTrainee,presavedTrainer));
        presavedTrainingSt = trainingDAO.createOrUpdateTraining(new Training("New training", trainingTypeSt,
                LocalDate.now(),3L,presavedTrainee,presavedTrainer));
    }

    @AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    public void testCreateTraining() {
        Training training = new Training("New training AG", trainingTypeSt,
                LocalDate.now(),1L,presavedTrainee,presavedTrainer);
        Training savedTraining = trainingService.createOrUpdateTraining(training);

        assertNotNull(savedTraining);
        assertEquals(training.getTrainingName(), savedTraining.getTrainingName());
    }

    @Test
    public void testGetTraining() {
        Training found = trainingService.getTraining(1L);

        assertNotNull(found);
        assertEquals(found.getTrainingName(), presavedTrainingAg.getTrainingName());
    }

    @Test
    public void testGetAllTrainings() {
        List<Training> found = trainingService.getAllTrainings();

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(t -> t.getId().equals(1L)));
    }

    @Test
    public void testGetTrainingsFiltered() {
        List<Training> trainings = trainingService.getTrainerTrainingsByCriteria(
                "John.Doe",null,null,null,trainingTypeAg);

        assertFalse(trainings.isEmpty());
        assertTrue(trainings.stream().anyMatch(t -> t.getTrainingType().getTrainingTypeName()
                .equals(trainingTypeAg.getTrainingTypeName())));

        trainings = trainingService.getTrainerTrainingsByCriteria(
                "John.Doe",null,null,null,trainingTypeSt);

        assertFalse(trainings.isEmpty());
        assertTrue(trainings.stream().anyMatch(t -> t.getTrainingType().getTrainingTypeName()
                .equals(trainingTypeSt.getTrainingTypeName())));

    }
}
