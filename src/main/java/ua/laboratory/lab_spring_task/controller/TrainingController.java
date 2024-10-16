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

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/trainings")
public class TrainingController {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;
    @Autowired
    private TrainingServiceImpl trainingService;


    @PostMapping("/create")
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
    public ResponseEntity<Set<TrainingType>> getTrainingTypes(@RequestHeader String username,
                                                              @RequestHeader String password){
        Credentials credentials = new Credentials(username, password);

        Set<TrainingType> trainingTypes = trainingService.getAllTrainingTypes(credentials);
        return ResponseEntity.ok(trainingTypes);
    }
}
