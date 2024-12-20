package data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static data.TaskType.TASK;

public class Task implements Comparable<Task> {
    private final String title;
    private final String description;
    private int idOfTask;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description, Status status, int idOfTask) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.idOfTask = idOfTask;
    }

    public Task(String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, Status status, LocalDateTime startTime, Duration duration, int idOfTask) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.idOfTask = idOfTask;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getIdOfTask() {
        return idOfTask;
    }

    public void setIdOfTask(int idOfTask) {
        this.idOfTask = idOfTask;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public int compareTo(Task task) {
        return this.startTime.compareTo(task.startTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", idOfTask=" + idOfTask +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return idOfTask == task.idOfTask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOfTask);
    }
}
