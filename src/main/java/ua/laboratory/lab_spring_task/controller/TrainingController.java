package ua.laboratory.lab_spring_task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.TrainingType;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.request.SearchCriteriaRequest;
import ua.laboratory.lab_spring_task.model.request.TrainingRegistrationRequest;
import ua.laboratory.lab_spring_task.model.response.TrainingDetailsResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;
import ua.laboratory.lab_spring_task.service.implementation.TrainingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/trainings")
@Tag(name = "Trainings", description = "Manage training sessions between trainees and trainers")
public class TrainingController {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainingServiceImpl trainingService;


    @PostMapping("/create")
    @Operation(
            summary = "Create a new training session",
            description = "Creates a training session by assigning a trainer to a trainee.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Training registration details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TrainingRegistrationRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Training created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    public ResponseEntity<Void> createTraining(@RequestHeader String username,
                                               @RequestHeader String password,
                                               @RequestBody TrainingRegistrationRequest request) {
        Credentials credentials = new Credentials(username, password);

        Trainee trainee = traineeService.getTraineeByUsername(request.getTraineeUsername(),credentials);
        Trainer trainer = trainerService.getTrainerByUsername(request.getTrainerUsername(),credentials);

        trainingService.createTraining(request.getTrainingName(), request.getTrainingDate(),
                request.getTrainingDuration(), request.getTrainingType(), trainee, trainer);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainee_trainings")
    @Operation(
            summary = "Get trainings for a trainee",
            description = "Fetches the list of trainings based on search criteria for a specific trainee.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Search criteria for fetching trainee trainings",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SearchCriteriaRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<List<TrainingDetailsResponse>> getTraineeTrainings(@RequestHeader String username,
                                                                        @RequestHeader String password,
                                                                        @RequestBody SearchCriteriaRequest request) {
        Credentials credentials = new Credentials(username, password);
        List<TrainingDetailsResponse> trainingDetailsResponse = trainingService.getTraineeTrainingsByCriteria(request.getUsername(),
                request.getFromDate(),request.getToDate(),request.getPartnerName(),
                        request.getType() == null ? null : request.getType().getTrainingTypeName(),
                        credentials).stream().map(training -> new TrainingDetailsResponse(training.getTrainingName(), training.getTrainingDate(),
                        training.getTrainingType(), training.getTrainingDuration(), training.getTrainer().getUser().getUsername()))
                .toList();
        return ResponseEntity.ok(trainingDetailsResponse);
    }

    @GetMapping("/trainer_trainings")
    @Operation(
            summary = "Get trainings for a trainer",
            description = "Fetches the list of trainings based on search criteria for a specific trainer.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Search criteria for fetching trainer trainings",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SearchCriteriaRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Trainings retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<List<TrainingDetailsResponse>> getTrainerTrainings(@RequestHeader String username,
                                                                             @RequestHeader String password,
                                                                             @RequestBody SearchCriteriaRequest request) {
        Credentials credentials = new Credentials(username, password);
        List<TrainingDetailsResponse> trainingDetailsResponse = trainingService.getTrainerTrainingsByCriteria(request.getUsername(),
                        request.getFromDate(),request.getToDate(),request.getPartnerName(),
                        request.getType() == null ? null : request.getType().getTrainingTypeName(),
                        credentials).stream().map(training -> new TrainingDetailsResponse(training.getTrainingName(), training.getTrainingDate(),
                        training.getTrainingType(), training.getTrainingDuration(), training.getTrainer().getUser().getUsername()))
                .toList();
        return ResponseEntity.ok(trainingDetailsResponse);
    }

    @GetMapping("/training_types")
    @Operation(
            summary = "Get all available training types",
            description = "Fetches the list of all training types available in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Training types retrieved successfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized access")
            }
    )
    public ResponseEntity<Set<TrainingType>> getTrainingTypes(@RequestHeader String username,
                                                              @RequestHeader String password){
        Credentials credentials = new Credentials(username, password);

        Set<TrainingType> trainingTypes = trainingService.getAllTrainingTypes(credentials);
        return ResponseEntity.ok(trainingTypes);
    }
}
