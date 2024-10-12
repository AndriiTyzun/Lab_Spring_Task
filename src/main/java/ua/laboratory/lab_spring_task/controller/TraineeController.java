package ua.laboratory.lab_spring_task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.request.TraineeRegistrationRequest;
import ua.laboratory.lab_spring_task.model.request.UpdateTraineeProfileRequest;
import ua.laboratory.lab_spring_task.model.response.TraineeProfileResponse;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createTrainee(@RequestBody TraineeRegistrationRequest request) {
        Trainee newTrainee = traineeService.createTrainee(request.getFirstName(), request.getLastName(),
                request.getDateOfBirth(), request.getAddress());

        Map<String, String> response = new HashMap<>();
        response.put("username", newTrainee.getUser().getUsername());
        response.put("password", newTrainee.getUser().getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<TraineeProfileResponse> getTraineeProfile(@RequestHeader String username,
                                                                    @RequestHeader String password) {
        Trainee trainee = traineeService.getTraineeByUsername(username, new Credentials(username, password));

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
    public ResponseEntity<TraineeProfileResponse> updateTraineeProfile(
            @RequestHeader String username,
            @RequestHeader String password,
            @RequestBody UpdateTraineeProfileRequest updateRequest) {

        Credentials credentials = new Credentials(username, password);
        Trainee trainee = traineeService.getTraineeByUsername(username, credentials);
        trainee.updateByRequest(updateRequest);
        Trainee updatedTrainee = traineeService.updateTrainee(trainee, credentials);

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
    public ResponseEntity<Void> deleteTraineeProfile(@RequestHeader String username,
                                                     @RequestHeader String password) {
        traineeService.deleteTrainee(username, new Credentials(username, password));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activateTrainee(@RequestHeader String username,
                                                     @RequestHeader String password) {
        traineeService.activateTrainee(username, new Credentials(username, password));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivateTrainee(@RequestHeader String username,
                                                @RequestHeader String password) {
        traineeService.deactivateTrainee(username, new Credentials(username, password));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/trainers")
    public ResponseEntity<Set<Trainer>> updateTrainers(@RequestHeader String username,
                                                        @RequestHeader String password,
                                                        @RequestBody List<String> trainers){
        Credentials credentials = new Credentials(username, password);
        Set<Trainer> trainerList = new HashSet<>();
        for (String trainer : trainers)
            trainerList.add(trainerService.getTrainerByUsername(trainer,credentials));

        traineeService.updateTrainers(traineeService.getTraineeByUsername(username, credentials).getId(),
                trainerList, credentials);

        trainerList = traineeService.getAllTrainers(username, credentials);
        return ResponseEntity.ok(trainerList);
    }







}
