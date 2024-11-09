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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.laboratory.lab_spring_task.model.dto.UserCredentials;
import ua.laboratory.lab_spring_task.model.request.ChangePasswordRequest;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.service.implementation.LoginAttemptService;
import ua.laboratory.lab_spring_task.service.implementation.LogoutService;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private LogoutService logoutService;

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
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    ));
            String token = jwtUtil.generateToken(authentication.getName());

            logInMetric.increment();
            loginAttemptService.resetAttempts(username);
            return ResponseEntity.ok().header(
                    HttpHeaders.AUTHORIZATION,
                    token
            ).body(
                    "Login successful"
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/logout")
    @Operation(
            summary = "User Logout",
            description = "Log out the user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout successful"),
                    @ApiResponse(responseCode = "401", description = "You aree not logged in")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        logoutService.blacklistToken(token);
        return ResponseEntity.ok("Successfully logged out");
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
    public ResponseEntity<String> changeTraineePassword(@AuthenticationPrincipal UserCredentials user, @RequestBody ChangePasswordRequest request) {
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
    public ResponseEntity<String> changeTrainerPassword(@AuthenticationPrincipal UserCredentials user, @RequestBody ChangePasswordRequest request) {
        trainerService.changePassword(user.getUsername(), request.getNewPassword());
        return ResponseEntity.ok("Password changed successfully");
    }

}
