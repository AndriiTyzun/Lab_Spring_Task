package ua.laboratory.lab_spring_task.model.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistrationRequest {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;

    @Override
    public String toString() {
        return "TraineeRegistrationRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                '}';
    }
}
