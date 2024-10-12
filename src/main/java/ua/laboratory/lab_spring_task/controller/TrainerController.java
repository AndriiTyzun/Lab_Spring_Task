package ua.laboratory.lab_spring_task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.laboratory.lab_spring_task.model.Trainee;
import ua.laboratory.lab_spring_task.model.Trainer;
import ua.laboratory.lab_spring_task.model.dto.Credentials;
import ua.laboratory.lab_spring_task.model.request.TrainerRegistrationRequest;
import ua.laboratory.lab_spring_task.model.response.TraineeProfileResponse;
import ua.laboratory.lab_spring_task.model.response.TrainerProfileResponse;
import ua.laboratory.lab_spring_task.service.TraineeService;
import ua.laboratory.lab_spring_task.service.TrainerService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createTrainer(@RequestBody TrainerRegistrationRequest request) {
        Trainer newTrainer = trainerService.createTrainer(request.getFirstName(), request.getLastName(),
                request.getTrainingType());

        Map<String, String> response = new HashMap<>();
        response.put("username", newTrainer.getUser().getUsername());
        response.put("password", newTrainer.getUser().getPassword());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/activate")
    public ResponseEntity<Void> activateTrainer(@RequestHeader String username,
                                                @RequestHeader String password) {
        trainerService.activateTrainer(username, new Credentials(username, password));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<Void> deactivateTrainer(@RequestHeader String username,
                                                @RequestHeader String password) {
        trainerService.deactivateTrainer(username, new Credentials(username, password));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trainers")
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
    public ResponseEntity<TrainerProfileResponse> getTraineeProfile(@RequestHeader String username,
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

    @PutMapping("/trainees")
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
