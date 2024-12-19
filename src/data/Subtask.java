package data;

import java.time.Duration;
import java.time.LocalDateTime;

import static data.TaskType.SUBTASK;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, Status status, LocalDateTime startTime, Duration duration, int epicId) {
        super(title, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status, LocalDateTime startTime, Duration duration, int epicId, int idOfTask) {
        super(title, description, status, startTime, duration, idOfTask);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", idOfTask=" + super.getIdOfTask() +
                ", status=" + super.getStatus() +
                ", startTime=" + super.getStartTime() +
                ", duration=" + super.getDuration() +
                ", epicId=" + epicId +
                '}';
    }
}
