package ua.laboratory.lab_spring_task.Model;

public class TrainingType {
    private String TrainingTypeName;

    public TrainingType() {
    }

    public TrainingType(String trainingTypeName) {
        TrainingTypeName = trainingTypeName;
    }

    public String getTrainingTypeName() {
        return TrainingTypeName;
    }

    public void setTrainingTypeName(String trainingTypeName) {
        TrainingTypeName = trainingTypeName;
    }
}
