package ua.laboratory.lab_spring_task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.request.ChangePasswordRequest;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.util.JwtUtil;
import ua.laboratory.lab_spring_task.util.metrics.LogInMetric;


@RestController
@RequestMapping("/api")
@Tag(name = "Authentication", description = "Endpoints for user authentication and password management.")
public class MainController {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private LogInMetric logInMetric;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    @Operation(
            summary = "User Login",
            description = "Validates the provided username and password.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials")
            }
    )
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Credentials credentials = new Credentials(username, password);

        Boolean isValid = traineeService.checkCredentials(credentials);
        if (isValid) {
            logInMetric.increment();

            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok().header(
                    HttpHeaders.AUTHORIZATION,
                    token
            ).body(
                    "Login successful"
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PutMapping("/change-trainee-password")
    @Operation(
            summary = "Change Trainee Password",
            description = "Allows a trainee to change their password.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description  = "ChangePasswordRequest object containing username, old password, and new password.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ChangePasswordRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed successfully"),
                    @ApiResponse(responseCode = "401", description = "Invalid old credentials")
            }
    )
    public ResponseEntity<String> changeTraineePassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordRequest request) {
        traineeService.changePassword(user.getUsername(), request.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }

    @PutMapping("/change-trainer-password")
    @Operation(
            summary = "Change Trainer Password",
            description = "Allows a trainer to change their password.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ChangePasswordRequest object containing username, old password, and new password.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ChangePasswordRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Password changed successfully"),
                    @ApiResponse(responseCode = "401", description = "Invalid old credentials")
            }
    )
    public ResponseEntity<String> changeTrainerPassword(@AuthenticationPrincipal User user, @RequestBody ChangePasswordRequest request) {
        trainerService.changePassword(user.getUsername(), request.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }

}
