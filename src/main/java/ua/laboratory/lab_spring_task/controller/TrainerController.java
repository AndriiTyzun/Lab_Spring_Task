package ua.laboratory.lab_spring_task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.dto.UserCredentials;
import ua.laboratory.lab_spring_task.model.request.TrainerRegistrationRequest;
import ua.laboratory.lab_spring_task.model.request.UpdateTrainerProfileRequest;
import ua.laboratory.lab_spring_task.model.response.TraineeProfileResponse;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.util.Utilities;
import ua.laboratory.lab_spring_task.util.metrics.RegistrationMetric;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainers")
@Tag(name = "Trainers", description = "Manage trainers and their trainees")
public class TrainerController {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private RegistrationMetric registrationMetric;

    @PostMapping("/create")
    @Operation(
            summary = "Create a new Trainer",
            description = "Registers a new trainer and returns login credentials.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainer registration details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TrainerRegistrationRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    public ResponseEntity<Map<String, String>> createTrainer(@RequestBody TrainerRegistrationRequest request) {
        String password = Utilities.generatePassword(10);
        TrainerProfileResponse newTrainer = trainerService.createTrainer(request.getFirstName(), request.getLastName(),
                request.getTrainingType(), password);

        Map<String, String> response = new HashMap<>();
        response.put("username", newTrainer.getUsername());
        response.put("password", password);

        registrationMetric.increment();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/activate")
    @Operation(
            summary = "Activate Trainer",
            description = "Activates the profile of the authenticated trainer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer activated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Void> activateTrainer(@AuthenticationPrincipal UserCredentials user) {
        trainerService.activateTrainer(user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    @Operation(
            summary = "Deactivate Trainer",
            description = "Deactivates the profile of the authenticated trainer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainer deactivated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Void> deactivateTrainer(@AuthenticationPrincipal UserCredentials user) {
        trainerService.deactivateTrainer(user.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainers")
    @Operation(
            summary = "Get Available Trainers",
            description = "Retrieves a list of trainers not assigned to a given trainee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainers retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Set<TrainerProfileResponse>> getAvailableTrainers(@AuthenticationPrincipal UserCredentials user,
                                                              @RequestBody String searchUsername){
        Set<TrainerProfileResponse> response = trainerService
                .getUnassignedTrainersByTraineeUsername(searchUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    @Operation(
            summary = "Get Trainer Profile",
            description = "Retrieves the profile details of the authenticated trainer.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(@AuthenticationPrincipal UserCredentials user) {
        TrainerProfileResponse trainer = trainerService.getTrainerByUsername(user.getUsername());
        return ResponseEntity.ok(trainer);
    }

    @PutMapping("/profile")
    @Operation(
            summary = "Update Trainer Profile",
            description = "Updates the profile of the authenticated trainer.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated profile details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateTrainerProfileRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<TrainerProfileResponse> updateTrainerProfile(
            @AuthenticationPrincipal UserCredentials user,
            @RequestBody UpdateTrainerProfileRequest updateRequest) {

        TrainerProfileResponse trainer = trainerService.getTrainerByUsername(user.getUsername());
        Trainer updatedtrainer = trainerService.getTrainerById(trainer.getTrainerId());
        updatedtrainer.updateByRequest(updateRequest);
        trainer = trainerService.updateTrainer(updatedtrainer);

        return ResponseEntity.ok(trainer);
    }

    @PutMapping("/trainees")
    @Operation(
            summary = "Update Assigned Trainees",
            description = "Updates the list of trainees assigned to the trainer.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of trainee usernames to be assigned",
                    required = true,
                    content = @Content(schema = @Schema(implementation = List.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainees updated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Set<TraineeProfileResponse>> updateTrainees(@AuthenticationPrincipal UserCredentials user,
                                                       @RequestBody List<String> trainees){
        Set<Trainee> traineeList = new HashSet<>();
        for (String trainer : trainees){
            TraineeProfileResponse trainee = traineeService.getTraineeByUsername(trainer);
            traineeList.add(traineeService.getTraineeById(trainee.getTraineeId()));
        }
        trainerService.updateTrainees(traineeService.getTraineeByUsername(
                user.getUsername()).getTraineeId(), traineeList);

        return ResponseEntity.ok(trainerService.getAllTrainees(user.getUsername()));
    }
}
