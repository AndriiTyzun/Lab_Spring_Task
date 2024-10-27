package ua.laboratory.lab_spring_task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.laboratory.lab_spring_task.model.dto.Credentials;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping("/check")
    @Operation(
            summary = "Health check",
            description = "Checks if API responds.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Check successful"),
                    @ApiResponse(responseCode = "500", description = "Check unsuccessful")
            }
    )
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Check successful");
    }
}
