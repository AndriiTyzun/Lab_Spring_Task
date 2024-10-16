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
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.request.TraineeRegistrationRequest;
import ua.laboratory.lab_spring_task.model.request.TrainerRegistrationRequest;
import ua.laboratory.lab_spring_task.model.request.UpdateTrainerProfileRequest;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeService traineeService;

    @MockBean
    private TrainerService trainerService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testCreateTrainer() throws Exception {
        TrainerRegistrationRequest request = new TrainerRegistrationRequest("John", "Doe", new TrainingType());

        when(trainerService.createTrainer(any(), any(), any())).thenReturn(
                new Trainer(1L,new TrainingType(),
                        new User("","","trainer1","password123",true),
                        new HashSet<>())
        );

        mockMvc.perform(post("/api/trainers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("trainer1"))
                .andExpect(jsonPath("$.password").value("password123"));
    }

    @Test
    public void testActivateTrainer() throws Exception {
        doNothing().when(trainerService).activateTrainer(anyString(), any());

        mockMvc.perform(patch("/api/trainers/activate")
                        .header("username", "trainer1")
                        .header("password", "password123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeactivateTrainer() throws Exception {
        doNothing().when(trainerService).deactivateTrainer(anyString(), any());

        mockMvc.perform(patch("/api/trainers/deactivate")
                        .header("username", "trainer1")
                        .header("password", "password123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAvailableTrainers() throws Exception {
        Set<Trainer> trainers = new HashSet<>();
        trainers.add(new Trainer(1L,new TrainingType(),
                new User("","","trainer1","password123",true),
                new HashSet<>()));

        when(trainerService.getUnassignedTrainersByTraineeUsername(anyString(), any()))
                .thenReturn(trainers);

        mockMvc.perform(get("/api/trainers/trainers")
                        .header("username", "user1")
                        .header("password", "password123")
                        .content("\"searchUsername\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("trainer1"));
    }

    @Test
    public void testGetTrainerProfile() throws Exception {
        when(trainerService.getTrainerByUsername(anyString(), any())).thenReturn(new Trainer(1L,new TrainingType(),
                new User("","","trainer1","password123",true),
                new HashSet<>()));

        mockMvc.perform(get("/api/trainers/profile")
                        .header("username", "trainer1")
                        .header("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("trainer1"));
    }

    @Test
    public void testUpdateTrainerProfile() throws Exception {
        UpdateTrainerProfileRequest updateRequest = new UpdateTrainerProfileRequest("John", "UpdatedDoe",
                new TrainingType(),true);
        Trainer trainer = new Trainer(1L,new TrainingType(),
                new User("","","trainer1","password123",true),
                new HashSet<>());

        when(trainerService.getTrainerByUsername(anyString(), any())).thenReturn(trainer);
        when(trainerService.updateTrainer(any(), any())).thenReturn(trainer);

        mockMvc.perform(put("/api/trainers/profile")
                        .header("username", "trainer1")
                        .header("password", "password123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateTrainees() throws Exception {
        List<String> traineeUsernames = List.of("trainee1", "trainee2");
        Set<Trainee> trainees = new HashSet<>();

        when(trainerService.getAllTrainees(anyString(), any())).thenReturn(trainees);
        when(trainerService.getTrainerByUsername(anyString(), any())).thenReturn(
                new Trainer(1L,new TrainingType(),
                        new User("","","trainer1","password123",true),
                        new HashSet<>())
        );
        when(traineeService.getTraineeByUsername(anyString(), any())).thenReturn(
                new Trainee(1L,LocalDate.now(), "Address 1",
                        new User("","","john123","password123",true),
                        new HashSet<>()
                ));


        mockMvc.perform(put("/api/trainers/trainees")
                        .header("username", "trainer1")
                        .header("password", "password123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeUsernames)))
                .andExpect(status().isOk());
    }

}
