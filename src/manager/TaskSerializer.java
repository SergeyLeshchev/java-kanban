package manager;

import data.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static data.TaskType.EPIC;

public class TaskSerializer {
    public static String toStringTask(Task task) {
        StringBuilder taskString = new StringBuilder(task.getIdOfTask() + ",");
        switch (task.getType()) {
            case TASK -> taskString.append("TASK,");
            case EPIC -> taskString.append("EPIC,");
            case SUBTASK -> taskString.append("SUBTASK,");
        }
        taskString.append(task.getTitle()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",");
        // Для эпиков
        if (task.getType().equals(EPIC)) {
            Epic epic = (Epic) task;
            if (epic.getSubtasksIds().isEmpty()) {
                taskString.append("null,0,null,"); // Если у эпика нет подзадач, то его startTime, duration и endTime = 0
            } else {
                taskString.append(task.getStartTime()).append(",")
                        .append(task.getDuration().toMinutes()).append(",")
                        .append(epic.getEndTime());
            }
            // Для не эпиков
        } else {
            taskString.append(task.getStartTime()).append(",")
                    .append(task.getDuration().toMinutes()).append(",");
        }
        // Для подзадач
        if (task.getType().equals(TaskType.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            taskString.append(subtask.getEpicId());
        }
        return taskString.toString();
    }

    public static Task fromString(String value) {
        String[] taskArray = value.split(",");
        int idOfTask = Integer.parseInt(taskArray[0]);
        TaskType type = TaskType.valueOf(taskArray[1]);
        String title = taskArray[2];
        Status status = Status.valueOf(taskArray[3]);
        String description = taskArray[4];
        LocalDateTime startTime = (taskArray[5].equals("null")) ? null : LocalDateTime.parse(taskArray[5]);
        Duration duration = Duration.ofMinutes(Long.parseLong(taskArray[6]));

        Task task = null;
        switch (type) {
            case TASK -> task = new Task(title, description, status, startTime, duration, idOfTask);
            case EPIC -> {
                LocalDateTime endTime = (taskArray[7].equals("null")) ? null : LocalDateTime.parse(taskArray[7]);
                task = new Epic(title, description, status, startTime, duration, endTime, idOfTask);
            }
            case SUBTASK -> task = new Subtask(title, description, status, startTime,
                    duration, Integer.parseInt(taskArray[7]), idOfTask);
        }
        return task;
    }
}
