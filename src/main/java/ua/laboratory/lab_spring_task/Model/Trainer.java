package ua.laboratory.lab_spring_task.Model;

public class Trainer extends User{
    private Long userId;
    private String specialization;

    public Trainer() {
    }

    public Trainer(String firstName, String lastName, String username, String password,
                   boolean isActive, long userId, String specialization) {
        super(firstName, lastName, username, password, isActive);
        this.userId = userId;
        this.specialization = specialization;
    }

    public Trainer(String firstName, String lastName,
                   boolean isActive, long userId, String specialization) {
        super(firstName, lastName, isActive);
        this.userId = userId;
        this.specialization = specialization;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
