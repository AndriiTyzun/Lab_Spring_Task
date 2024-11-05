package ua.laboratory.lab_spring_task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.User;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.request.TraineeRegistrationRequest;
import ua.laboratory.lab_spring_task.model.request.UpdateTraineeProfileRequest;
import ua.laboratory.lab_spring_task.model.response.TraineeProfileResponse;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.util.metrics.LogInMetric;
import ua.laboratory.lab_spring_task.util.metrics.RegistrationMetric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainees")
@Tag(name = "Trainees", description = "Manage trainee profiles, trainers, and activation status")
public class TraineeController {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private RegistrationMetric registrationMetric;

    @PostMapping("/create")
    @Operation(
            summary = "Create a new Trainee",
            description = "Registers a new trainee and returns login credentials.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Trainee registration details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TraineeRegistrationRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainee created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    public ResponseEntity<Map<String, String>> createTrainee(@RequestBody TraineeRegistrationRequest request) {
        Trainee newTrainee = traineeService.createTrainee(request.getFirstName(), request.getLastName(),
                request.getDateOfBirth(), request.getAddress());

        Map<String, String> response = new HashMap<>();
        response.put("username", newTrainee.getUser().getUsername());
        response.put("password", newTrainee.getUser().getPassword());

        registrationMetric.increment();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    @Operation(
            summary = "Get Trainee Profile",
            description = "Retrieves the profile details of the authenticated trainee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<TraineeProfileResponse> getTraineeProfile(@AuthenticationPrincipal User user) {
        Trainee trainee = traineeService.getTraineeByUsername(user.getUsername());

        List<TrainerProfileResponse> trainers = trainee.getTrainers().stream()
                .map(trainer -> new TrainerProfileResponse(trainer.getUser().getUsername(),
                        trainer.getUser().getFirstName(), trainer.getUser().getLastName(),
                        trainer.getUser().isActive(),trainer.getSpecialization(),null))
                .collect(Collectors.toList());

        TraineeProfileResponse response = new TraineeProfileResponse(
                trainee.getUser().getUsername(),
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getUser().isActive(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainers
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    @Operation(
            summary = "Update Trainee Profile",
            description = "Updates the profile information of a trainee.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Update request containing new profile details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateTraineeProfileRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<TraineeProfileResponse> updateTraineeProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateTraineeProfileRequest updateRequest) {

        Trainee trainee = traineeService.getTraineeByUsername(user.getUsername());
        trainee.updateByRequest(updateRequest);
        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        List<TrainerProfileResponse> trainers = updatedTrainee.getTrainers().stream()
                .map(trainer -> new TrainerProfileResponse(trainer.getUser().getUsername(),
                        trainer.getUser().getFirstName(), trainer.getUser().getLastName(),
                        trainer.getUser().isActive(),trainer.getSpecialization(),null))
                .collect(Collectors.toList());

        TraineeProfileResponse response = new TraineeProfileResponse(
                updatedTrainee.getUser().getUsername(),
                updatedTrainee.getUser().getFirstName(),
                updatedTrainee.getUser().getLastName(),
                updatedTrainee.getUser().isActive(),
                updatedTrainee.getDateOfBirth(),
                updatedTrainee.getAddress(),
                trainers
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/")
    @Operation(
            summary = "Delete Trainee Profile",
            description = "Deletes the profile of the authenticated trainee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Void> deleteTraineeProfile(@AuthenticationPrincipal User user) {
        traineeService.deleteTrainee(user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate")
    @Operation(
            summary = "Activate Trainee",
            description = "Activates the profile of the authenticated trainee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile activated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Void> activateTrainee(@AuthenticationPrincipal User user) {
        traineeService.activateTrainee(user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    @Operation(
            summary = "Deactivate Trainee",
            description = "Deactivates the profile of the authenticated trainee.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile deactivated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Void> deactivateTrainee(@AuthenticationPrincipal User user) {
        traineeService.deactivateTrainee(user.getUsername());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/trainers")
    @Operation(
            summary = "Update Trainers",
            description = "Updates the list of trainers assigned to the trainee.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of trainer usernames to be assigned",
                    required = true,
                    content = @Content(schema = @Schema(implementation = List.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainers updated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Set<Trainer>> updateTrainers(@AuthenticationPrincipal User user,
                                                        @RequestBody List<String> trainers){

        Set<Trainer> trainerList = new HashSet<>();
        for (String trainer : trainers)
            trainerList.add(trainerService.getTrainerByUsername(trainer));

        traineeService.updateTrainers(traineeService.getTraineeByUsername(user.getUsername()).getId(),
                trainerList);

        trainerList = traineeService.getAllTrainers(user.getUsername());
        return ResponseEntity.ok(trainerList);
    }
}
