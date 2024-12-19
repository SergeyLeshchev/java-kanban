package data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static data.TaskType.EPIC;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public Epic(String title, String description, Status status, LocalDateTime startTime, Duration duration,
                LocalDateTime endTime, int idOfTask) {
        super(title, description, status, startTime, duration, idOfTask);
        this.endTime = endTime;
    }

    public Epic(String title, String description, Status status, LocalDateTime startTime, Duration duration,
                ArrayList<Integer> subtasksIds, int idOfTask) {
        super(title, description, status, startTime, duration, idOfTask);
        this.subtasksIds = subtasksIds;
    }

    public TaskType getType() {
        return EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtaskId(int idOfSubtask) {
        subtasksIds.add(idOfSubtask);
    }

    public void removeSubtaskId(Integer idOfSubtask) {
        subtasksIds.remove(idOfSubtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", idOfTask=" + super.getIdOfTask() +
                ", status=" + super.getStatus() +
                ", startTime=" + super.getStartTime() +
                ", duration=" + super.getDuration() +
                ", endTime=" + endTime +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
