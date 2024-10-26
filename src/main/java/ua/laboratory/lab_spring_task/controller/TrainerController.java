package ua.laboratory.lab_spring_task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.request.TrainerRegistrationRequest;
import ua.laboratory.lab_spring_task.model.request.UpdateTrainerProfileRequest;
import ua.laboratory.lab_spring_task.model.response.TraineeProfileResponse;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.util.metrics.RegistrationMetric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

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
            requestBody = @RequestBody(
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
        Trainer newTrainer = trainerService.createTrainer(request.getFirstName(), request.getLastName(),
                request.getTrainingType());

        Map<String, String> response = new HashMap<>();
        response.put("username", newTrainer.getUser().getUsername());
        response.put("password", newTrainer.getUser().getPassword());

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
    public ResponseEntity<Void> activateTrainer(@RequestHeader String username,
                                                @RequestHeader String password) {
        trainerService.activateTrainer(username, new Credentials(username, password));
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
    public ResponseEntity<Void> deactivateTrainer(@RequestHeader String username,
                                                @RequestHeader String password) {
        trainerService.deactivateTrainer(username, new Credentials(username, password));
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
    public ResponseEntity<Set<TrainerProfileResponse>> getAvailableTrainers(@RequestHeader String username,
                                                              @RequestHeader String password,
                                                              @RequestBody String searchUsername){
        Set<TrainerProfileResponse> response = trainerService.getUnassignedTrainersByTraineeUsername(
                searchUsername, new Credentials(username, password)).stream()
                .filter(trainer -> trainer.getUser().isActive())
                .map(trainer -> new TrainerProfileResponse(trainer.getUser().getUsername(),
                        trainer.getUser().getFirstName(), trainer.getUser().getLastName(),
                        true,trainer.getSpecialization(),null))
                .collect(Collectors.toSet());;
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
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(@RequestHeader String username,
                                                                    @RequestHeader String password) {
        Trainer trainer = trainerService.getTrainerByUsername(username, new Credentials(username, password));

        List<TraineeProfileResponse> trainers = trainer.getTrainees().stream()
                .map(trainee -> new TraineeProfileResponse(trainee.getUser().getUsername(),
                        trainee.getUser().getFirstName(), trainee.getUser().getLastName(),
                        trainee.getUser().isActive(), trainee.getDateOfBirth(),
                        trainee.getAddress(), null))
                .collect(Collectors.toList());

        TrainerProfileResponse response = new TrainerProfileResponse(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().isActive(),
                trainer.getSpecialization(),
                trainers
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    @Operation(
            summary = "Update Trainer Profile",
            description = "Updates the profile of the authenticated trainer.",
            requestBody = @RequestBody(
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
            @RequestHeader String username,
            @RequestHeader String password,
            @RequestBody UpdateTrainerProfileRequest updateRequest) {

        Credentials credentials = new Credentials(username, password);
        Trainer trainer = trainerService.getTrainerByUsername(username, credentials);
        trainer.updateByRequest(updateRequest);
        Trainer updatedTrainer = trainerService.updateTrainer(trainer, credentials);

        List<TraineeProfileResponse> trainees = updatedTrainer.getTrainees().stream()
                .map(trainee -> new TraineeProfileResponse(trainee.getUser().getUsername(),
                        trainee.getUser().getFirstName(), trainee.getUser().getLastName(),
                        trainee.getUser().isActive(),trainee.getDateOfBirth(),trainee.getAddress(),
                        null))
                .toList();

        TrainerProfileResponse response = new TrainerProfileResponse(
                updatedTrainer.getUser().getUsername(),
                updatedTrainer.getUser().getFirstName(),
                updatedTrainer.getUser().getLastName(),
                updatedTrainer.getUser().isActive(),
                updatedTrainer.getSpecialization(),
                trainees
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/trainees")
    @Operation(
            summary = "Update Assigned Trainees",
            description = "Updates the list of trainees assigned to the trainer.",
            requestBody = @RequestBody(
                    description = "List of trainee usernames to be assigned",
                    required = true,
                    content = @Content(schema = @Schema(implementation = List.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainees updated successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Set<Trainee>> updateTrainees(@RequestHeader String username,
                                                       @RequestHeader String password,
                                                       @RequestBody List<String> trainers){
        Credentials credentials = new Credentials(username, password);
        Set<Trainee> traineeList = new HashSet<>();
        for (String trainer : trainers)
            traineeList.add(traineeService.getTraineeByUsername(trainer,credentials));

        trainerService.updateTrainees(traineeService.getTraineeByUsername(username, credentials).getId(),
                traineeList, credentials);

        traineeList = trainerService.getAllTrainees(username, credentials);
        return ResponseEntity.ok(traineeList);
    }
}
