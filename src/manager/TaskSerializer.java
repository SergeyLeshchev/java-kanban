package manager;

import data.*;

public class TaskSerializer {
    public static String toStringTask(Task task) {
        String taskString = task.getIdOfTask() + ",";
        switch (task.getType()) {
            case TASK -> taskString += "TASK,";
            case EPIC -> taskString += "EPIC,";
            case SUBTASK -> taskString += "SUBTASK,";
        }
        taskString += task.getTitle() + "," + task.getStatus() + "," + task.getDescription() + ",";
        if (task.getType().equals(TaskType.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            taskString += subtask.getEpicId();
        }
        return taskString;
    }

    public static Task fromString(String value) {
        String[] taskArray = value.split(",");
        int idOfTask = Integer.parseInt(taskArray[0]);
        TaskType type = TaskType.valueOf(taskArray[1]);
        String title = taskArray[2];
        Status status = Status.valueOf(taskArray[3]);
        String description = taskArray[4];
        // Здесь нельзя создать переменную int epicId, иначе если не subtask, то ArrayIndexOutOfBoundsException
        // Так как в массиве для subtask больше строк
        Task task = null;
        switch (type) {
            case TASK -> task = new Task(title, description, status, idOfTask);
            case EPIC -> task = new Epic(title, description, status, idOfTask);
            case SUBTASK -> task = new Subtask(title, description, status, Integer.parseInt(taskArray[5]), idOfTask);
        }
        return task;
    }
}