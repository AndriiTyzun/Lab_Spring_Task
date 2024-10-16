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
import ua.laboratory.lab_spring_task.model.request.UpdateTraineeProfileRequest;
import ua.laboratory.lab_spring_task.model.response.TraineeProfileResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TraineeControllerTests {
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
    public void testCreateTrainee() throws Exception {
        TraineeRegistrationRequest request = new TraineeRegistrationRequest("John", "Doe", LocalDate.now(), "Address");

        when(traineeService.createTrainee(any(), any(), any(), any())).thenReturn(
                new Trainee(1L,LocalDate.now(), "Address 1",
                        new User("","","john123","password123",true),
                        new HashSet<>())
        );

        mockMvc.perform(post("/api/trainees/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john123"))
                .andExpect(jsonPath("$.password").value("password123"));
    }

    @Test
    public void testGetTraineeProfile() throws Exception {
        when(traineeService.getTraineeByUsername(any(), any())).thenReturn(
                new Trainee(1L,LocalDate.now(), "Address 1",
                        new User("","","john123","password123",true),
                        new HashSet<>())
        );

        mockMvc.perform(get("/api/trainees/profile")
                        .header("username", "john123")
                        .header("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john123"));
    }

    @Test
    public void testUpdateTraineeProfile() throws Exception {
        UpdateTraineeProfileRequest updateRequest = new UpdateTraineeProfileRequest("Updated", "Doe", LocalDate.now(), "New Address",true);
        Trainee trainee = new Trainee(1L,LocalDate.now(), "Address 1",
                new User("","","john123","password123",true),
                new HashSet<>());

        when(traineeService.getTraineeByUsername(anyString(), any())).thenReturn(trainee);
        when(traineeService.updateTrainee(any(), any())).thenReturn(trainee);

        mockMvc.perform(put("/api/trainees/profile")
                        .header("username", "john123")
                        .header("password", "password123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTraineeProfile() throws Exception {
        doNothing().when(traineeService).deleteTrainee(anyString(), any());

        mockMvc.perform(delete("/api/trainees/")
                        .header("username", "john123")
                        .header("password", "password123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testActivateTrainee() throws Exception {
        doNothing().when(traineeService).activateTrainee(anyString(), any());

        mockMvc.perform(patch("/api/trainees/activate")
                        .header("username", "john123")
                        .header("password", "password123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeactivateTrainee() throws Exception {
        doNothing().when(traineeService).deactivateTrainee(anyString(), any());

        mockMvc.perform(patch("/api/trainees/deactivate")
                        .header("username", "john123")
                        .header("password", "password123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateTrainers() throws Exception {
        List<String> trainerUsernames = List.of("trainer1", "trainer2");
        Set<Trainer> trainerSet = new HashSet<>();

        when(trainerService.getTrainerByUsername(anyString(), any())).thenReturn(
                new Trainer(1L,new TrainingType(),
                        new User("","","john123","password123",true),
                        new HashSet<>())
        );
        when(traineeService.getAllTrainers(anyString(), any())).thenReturn(trainerSet);
        when(traineeService.getTraineeByUsername(anyString(), any())).thenReturn(
                new Trainee(1L,LocalDate.now(), "Address 1",
                        new User("","","john123","password123",true),
                        new HashSet<>()
        ));

        mockMvc.perform(put("/api/trainees/trainers")
                        .header("username", "john123")
                        .header("password", "password123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerUsernames)))
                .andExpect(status().isOk());
    }
}
