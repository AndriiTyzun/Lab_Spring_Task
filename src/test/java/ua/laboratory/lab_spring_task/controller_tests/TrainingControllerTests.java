package ua.laboratory.lab_spring_task.controller_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.laboratory.lab_spring_task.model.*;
import ua.laboratory.lab_spring_task.model.request.SearchCriteriaRequest;
import ua.laboratory.lab_spring_task.model.request.TrainingRegistrationRequest;
import ua.laboratory.lab_spring_task.model.response.TrainingDetailsResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.service.TrainingService;
import ua.laboratory.lab_spring_task.service.implementation.TrainingServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeService traineeService;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private TrainingServiceImpl trainingService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testCreateTraining() throws Exception {
        TrainingRegistrationRequest request = new TrainingRegistrationRequest(
                "john123", "trainer1", "Training 1", LocalDate.now(),
                2L, new TrainingType()
        );

        when(traineeService.getTraineeByUsername(anyString(), any())).thenReturn(
                new Trainee(1L, LocalDate.now(), "Address 1",
                        new User("","","john123","password123",true),
                        new HashSet<>())
        );
        when(trainerService.getTrainerByUsername(anyString(), any())).thenReturn(
                new Trainer(1L,new TrainingType(),
                        new User("","","trainer1","password123",true),
                        new HashSet<>())
        );

        mockMvc.perform(post("/api/trainings/create")
                        .header("username", "admin")
                        .header("password", "password123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTraineeTrainings() throws Exception {
        SearchCriteriaRequest criteria = new SearchCriteriaRequest(
                "trainee1", LocalDate.now().minusDays(10), LocalDate.now(), "trainer1", null
        );

        List<TrainingDetailsResponse> response = List.of(
                new TrainingDetailsResponse("Training1", LocalDate.now(), new TrainingType(), 2L, "trainer1")
        );

        when(trainingService.getTraineeTrainingsByCriteria(anyString(), any(), any(), any(), any(), any()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/trainings/trainee_trainings")
                        .header("username", "trainee1")
                        .header("password", "password123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criteria)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTrainerTrainings() throws Exception {
        SearchCriteriaRequest criteria = new SearchCriteriaRequest(
                "trainer1", LocalDate.now().minusDays(10), LocalDate.now(), "trainee1", null
        );

        List<TrainingDetailsResponse> response = List.of(
                new TrainingDetailsResponse("Training1", LocalDate.now(), new TrainingType(),
                        2L, "trainer1")
        );

        when(trainingService.getTrainerTrainingsByCriteria(anyString(), any(), any(), any(), any(), any()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/trainings/trainer_trainings")
                        .header("username", "trainer1")
                        .header("password", "password123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criteria)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTrainingTypes() throws Exception {
        Set<TrainingType> trainingTypes = Set.of(
                new TrainingType("Yoga"), new TrainingType("Cardio")
        );

        when(trainingService.getAllTrainingTypes(any())).thenReturn(trainingTypes);

        mockMvc.perform(get("/api/trainings/training_types")
                        .header("username", "admin")
                        .header("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingTypeName").value("Cardio"))
                .andExpect(jsonPath("$[1].trainingTypeName").value("Yoga"));
    }
}
