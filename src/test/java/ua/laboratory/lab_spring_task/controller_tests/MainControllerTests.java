package ua.laboratory.lab_spring_task.controller_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.request.ChangePasswordRequest;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeService traineeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testLogin_Success() throws Exception {
        String username = "john";
        String password = "password123";

        when(traineeService.checkCredentials(any(Credentials.class))).thenReturn(true);

        mockMvc.perform(get("/api/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        String username = "john";
        String password = "wrongPassword";

        when(traineeService.checkCredentials(new Credentials(username, password)))
                .thenReturn(false);

        mockMvc.perform(get("/api/login")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    public void testChangePassword_Success() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest("john", "oldPassword", "newPassword");

        when(traineeService.checkCredentials(any(Credentials.class))).thenReturn(true);

        mockMvc.perform(put("/api/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));

        verify(traineeService, times(1)).changePassword(
                eq(request.getUsername()), eq(request.getNewPassword()), any(Credentials.class));
    }

    @Test
    public void testChangePassword_InvalidOldCredentials() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest("john", "wrongOldPassword", "newPassword");

        when(traineeService.checkCredentials(new Credentials(request.getUsername(), request.getOldPassword())))
                .thenReturn(false);

        mockMvc.perform(put("/api/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid old credentials"));
    }
}
