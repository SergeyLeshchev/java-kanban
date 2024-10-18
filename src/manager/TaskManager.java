package manager;

import data.Epic;
import data.Subtask;
import data.Task;

import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task task);

    void updateTask(Task task);

    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void calculateEpicStatus(Epic epic);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    Task getTask(int idOfTask);

    Epic getEpic(int idOfTask);

    Subtask getSubtask(int idOfTask);

    void removeTask(int idOfTask);

    void removeEpic(int idOfTask);

    void removeSubtask(int idOfTask);

    ArrayList<Subtask> getSubtasksOfEpic(Epic epic);

    ArrayList<Task> getHistory();
}