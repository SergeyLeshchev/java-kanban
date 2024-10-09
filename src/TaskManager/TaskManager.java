package TaskManager;

import Data.Epic;
import Data.Status;
import Data.Subtask;
import Data.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskCollection = new HashMap<>();
    private HashMap<Integer, Epic> epicCollection = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskCollection = new HashMap<>();
    private int idOfTasks = 1;

    public void addTask(Task task) {
        task.setIdOfTask(idOfTasks);
        taskCollection.put(idOfTasks, task);
        idOfTasks++;
    }

    public void updateTask(Task task) {
        if (taskCollection.containsKey(task.getIdOfTask())) {
            taskCollection.put(task.getIdOfTask(), task);
        }
    }

    public void addEpic(Epic epic) {
        epic.setIdOfTask(idOfTasks);
        epicCollection.put(idOfTasks, epic);
        idOfTasks++;
    }

    public void updateEpic(Epic epic) {
        if (epicCollection.containsKey(epic.getIdOfTask())) {
            epicCollection.put(epic.getIdOfTask(), epic);
        }
    }

    public void addSubtask(Subtask subtask) {
        subtask.setIdOfTask(idOfTasks);
        subtaskCollection.put(idOfTasks, subtask);
        idOfTasks++;

        if (epicCollection.containsKey(subtask.getEpicID())) {
            Epic epic = epicCollection.get(subtask.getEpicID());
            if (epic != null) {
                epic.addSubtaskID(subtask.getIdOfTask());
                calculateEpicStatus(epic);
            }
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtaskCollection.containsKey(subtask.getIdOfTask())) {
            subtaskCollection.put(subtask.getIdOfTask(), subtask);
            calculateEpicStatus(epicCollection.get(subtask.getEpicID()));
        }
    }

    private void calculateEpicStatus(Epic epic) {
        if (epic.getSubtasksIds().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        Status status = Status.NEW;
        for (Integer idOfSubtask : epic.getSubtasksIds()) {
            if (subtaskCollection.get(idOfSubtask).getStatus() != Status.NEW) {
                status = Status.DONE;
                break;
            }
        }
        if (status == Status.NEW) {
            epic.setStatus(Status.NEW);
            return;
        }

        for (Integer idOfSubtask : epic.getSubtasksIds()) {
            if (subtaskCollection.get(idOfSubtask).getStatus() != Status.DONE) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        epic.setStatus(Status.DONE);
    }

    public ArrayList<Object> getAllTasks() {
        ArrayList<Object> allTasksList = new ArrayList<>(taskCollection.size() + epicCollection.size() +
                subtaskCollection.size());
        allTasksList.addAll(taskCollection.values());
        allTasksList.addAll(epicCollection.values());
        allTasksList.addAll(subtaskCollection.values());
        return allTasksList;
    }

    public void removeAllTasks() {
        taskCollection.clear();
    }

    public void removeAllEpics() {
        subtaskCollection.clear();
        epicCollection.clear();
    }

    public void removeAllSubtasks() {
        subtaskCollection.clear();
        for (Epic epic : epicCollection.values()) {
            epic.getSubtasksIds().clear();
        }
    }

    public Task getTaskByID(int idOfTask) {
        return taskCollection.get(idOfTask);
    }

    public Epic getEpicByID(int idOfTask) {
        return epicCollection.get(idOfTask);
    }

    public Subtask getSubtaskByID(int idOfTask) {
        return subtaskCollection.get(idOfTask);
    }

    public void removeTask(int idOfTask) {
        taskCollection.remove(idOfTask);
    }

    public void removeEpic(int idOfTask) {
        for (int keyOfSubtask : epicCollection.get(idOfTask).getSubtasksIds()) {
            subtaskCollection.remove(keyOfSubtask);
        }
        epicCollection.remove(idOfTask);
    }

    public void removeSubtask(int idOfTask) {
        int idOfEpic = subtaskCollection.get(idOfTask).getEpicID();
        subtaskCollection.remove(idOfTask);
        epicCollection.get(idOfEpic).removeSubtaskID(idOfTask);
        calculateEpicStatus(epicCollection.get(idOfEpic));
    }

    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>(epic.getSubtasksIds().size());
        for (Integer keySubtask : epic.getSubtasksIds()) {
            subtasksOfEpic.add(subtaskCollection.get(keySubtask));
        }
        return subtasksOfEpic;
    }
}