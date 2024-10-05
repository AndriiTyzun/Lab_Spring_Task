package ua.laboratory.lab_spring_task.serviceTests;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.laboratory.lab_spring_task.dao.*;
import ua.laboratory.lab_spring_task.dao.implementation.*;
import ua.laboratory.lab_spring_task.model.*;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.service.implementation.TraineeServiceImpl;
import ua.laboratory.lab_spring_task.service.implementation.TrainerServiceImpl;
import ua.laboratory.lab_spring_task.service.implementation.TrainingServiceImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingServiceTests {
    private TraineeServiceImpl traineeService;
    private TrainerServiceImpl trainerService;
    private TrainingServiceImpl trainingService;
    private static SessionFactory sessionFactory;
    private User presavedUser;
    private Trainee presavedTrainee;
    private Trainer presavedTrainer;
    private TrainingType trainingTypeAg;
    private TrainingType trainingTypeSt;
    private Training presavedTrainingAg;
    private Training presavedTrainingSt;
    private Credentials credentials;
    private UserRepository userDAO ;
    private TraineeRepository traineeDAO;
    private TrainerRepository trainerDAO;

    @BeforeEach
    public void init() {
        sessionFactory = new Configuration()
                .configure("hibernate-test.cfg.xml")
                .buildSessionFactory();
        userDAO = new UserDAOImpl(sessionFactory);
        TrainingTypeRepository trainingTypeDAO = new TrainingTypeDAOImpl(sessionFactory);
        traineeDAO = new TraineeDAOImpl(userDAO,sessionFactory);
        trainerDAO = new TrainerDAOImpl(userDAO, trainingTypeDAO, sessionFactory, traineeDAO);
        TrainingRepository trainingDAO = new TrainingDAOImpl(userDAO, trainingTypeDAO, traineeDAO, sessionFactory);
        traineeService = new TraineeServiceImpl(traineeDAO);
        trainerService = new TrainerServiceImpl(trainerDAO);
        trainingService = new TrainingServiceImpl(trainingDAO,userDAO);

        trainingTypeAg = trainingTypeDAO.addTrainingType( new TrainingType("Agility"));
        trainingTypeSt = trainingTypeDAO.addTrainingType( new TrainingType("Strength"));

        presavedUser = userDAO.createOrUpdateUser(new User("John", "Doe","j.d","1233211231", true));
        presavedTrainee = traineeDAO.createOrUpdateTrainee(new Trainee(LocalDate.now(), "City, Street, House 1", presavedUser));
        presavedTrainer = trainerDAO.createOrUpdateTrainer(new Trainer(trainingTypeAg, presavedUser));

        presavedTrainingAg = trainingDAO.createOrUpdateTraining(new Training("New training", trainingTypeAg,
                LocalDate.now(),2L,presavedTrainee,presavedTrainer));
        presavedTrainingSt = trainingDAO.createOrUpdateTraining(new Training("New training", trainingTypeSt,
                LocalDate.now(),3L,presavedTrainee,presavedTrainer));
        credentials = new Credentials(presavedTrainer.getUser().getUsername(),presavedTrainer.getUser().getPassword());
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
        Training found = trainingService.getTraining(1L,credentials);

        assertNotNull(found);
        assertEquals(found.getTrainingName(), presavedTrainingAg.getTrainingName());
    }

    @Test
    public void testGetAllTrainings() {
        List<Training> found = trainingService.getAllTrainings(credentials);

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(t -> t.getId().equals(1L)));
    }

    @Test
    public void testGetTrainingsFiltered() {
        User anotherUser = userDAO.createOrUpdateUser(new User("John", "Smith","j.s","1233211231", true));
        Trainee anotherTrainee = traineeDAO.createOrUpdateTrainee(new Trainee(LocalDate.now(), "City, Street, House 1", anotherUser));
        Trainer anotherTrainer = trainerDAO.createOrUpdateTrainer(new Trainer(trainingTypeAg, anotherUser));
        Training anotherTraining = trainingService.createOrUpdateTraining(new Training("New training", trainingTypeAg,
                LocalDate.now(),2L,anotherTrainee,anotherTrainer));


        List<Training> trainings = trainingService.getTraineeTrainingsByCriteria(
                "j.s",null,null,"j.s",credentials);

        assertFalse(trainings.isEmpty());
        assertTrue(trainings.stream().anyMatch(t -> t.getTrainee().getUser().getUsername()
                .equals(anotherUser.getUsername())));

        trainings = trainingService.getTrainerTrainingsByCriteria(
                "j.s",null,null,"j.s",credentials);

        assertFalse(trainings.isEmpty());
        assertTrue(trainings.stream().anyMatch(t -> t.getTrainer().getUser().getUsername()
                .equals(anotherUser.getUsername())));

    }
}
