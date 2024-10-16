package ua.laboratory.lab_spring_task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.request.ChangePasswordRequest;
import ua.laboratory.lab_spring_task.model.request.TraineeRegistrationRequest;
import ua.laboratory.lab_spring_task.model.request.TrainerRegistrationRequest;
import ua.laboratory.lab_spring_task.model.request.UpdateTraineeProfileRequest;
import ua.laboratory.lab_spring_task.model.response.TraineeProfileResponse;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        Credentials credentials = new Credentials(username, password);

        Boolean isValid = traineeService.checkCredentials(credentials);
        if (isValid) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PutMapping("/change-trainee-password")
    public ResponseEntity<String> changeTraineePassword(@RequestBody ChangePasswordRequest request) {
        Credentials oldCredentials = new Credentials(request.getUsername(), request.getOldPassword());

        Boolean isValid = traineeService.checkCredentials(oldCredentials);

        if (isValid) {
            traineeService.changePassword(request.getUsername(), request.getNewPassword(), oldCredentials);
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old credentials");
        }
    }

    @PutMapping("/change-trainer-password")
    public ResponseEntity<String> changeTrainerPassword(@RequestBody ChangePasswordRequest request) {
        Credentials oldCredentials = new Credentials(request.getUsername(), request.getOldPassword());

        Boolean isValid = traineeService.checkCredentials(oldCredentials);

        if (isValid) {
            trainerService.changePassword(request.getUsername(), request.getNewPassword(), oldCredentials);
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old credentials");
        }
    }

}
