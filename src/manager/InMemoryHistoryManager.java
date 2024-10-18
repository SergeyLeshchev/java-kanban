package manager;

import data.Epic;
import data.Subtask;
import data.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> history = new ArrayList<>(10);

    @Override
    public void addTaskInHistory(Task task) {

        Task newTask;
        if (task.getClass() == Task.class) {
            newTask = new Task(task.getTitle(), task.getDescription(), task.getStatus(), task.getIdOfTask());
        } else if (task.getClass() == Epic.class) {
            newTask = new Epic(task.getTitle(), task.getDescription(), task.getStatus(), ((Epic) task).getSubtasksIds(), task.getIdOfTask());
        } else {
            newTask = new Subtask(task.getTitle(), task.getDescription(), task.getStatus(), ((Subtask) task).getEpicId(), task.getIdOfTask());
        }

        if (history.size() > 9) {
            history.removeFirst();
        }
        history.add(newTask);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}