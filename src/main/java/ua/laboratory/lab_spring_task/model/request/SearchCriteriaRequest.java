package ua.laboratory.lab_spring_task.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.laboratory.lab_spring_task.model.TrainingType;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaRequest {
    private String username;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String partnerName;
    private TrainingType type;

}
