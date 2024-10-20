package manager;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private final HashMap<Integer, Task> taskCollection = new HashMap<>();
    private final HashMap<Integer, Epic> epicCollection = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskCollection = new HashMap<>();
    private int idOfTasks = 1;

    @Override
    public void addTask(Task task) {
        task.setIdOfTask(idOfTasks);
        taskCollection.put(idOfTasks, task);
        idOfTasks++;
    }

    @Override
    public void updateTask(Task task) {
        if (taskCollection.containsKey(task.getIdOfTask())) {
            taskCollection.put(task.getIdOfTask(), task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setIdOfTask(idOfTasks);
        epicCollection.put(idOfTasks, epic);
        idOfTasks++;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicCollection.containsKey(epic.getIdOfTask())) {
            epicCollection.put(epic.getIdOfTask(), epic);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setIdOfTask(idOfTasks);
        subtaskCollection.put(idOfTasks, subtask);
        idOfTasks++;

        if (epicCollection.containsKey(subtask.getEpicId())) {
            Epic epic = epicCollection.get(subtask.getEpicId());
            if (epic != null) {
                epic.addSubtaskId(subtask.getIdOfTask());
                calculateEpicStatus(epic);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskCollection.containsKey(subtask.getIdOfTask())) {
            subtaskCollection.put(subtask.getIdOfTask(), subtask);
            calculateEpicStatus(epicCollection.get(subtask.getEpicId()));
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

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskCollection.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicCollection.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskCollection.values());
    }

    @Override
    public void removeAllTasks() {
        taskCollection.clear();
    }

    @Override
    public void removeAllEpics() {
        subtaskCollection.clear();
        epicCollection.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtaskCollection.clear();
        for (Epic epic : epicCollection.values()) {
            epic.getSubtasksIds().clear();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public Task getTask(int idOfTask) {
        Task task = taskCollection.get(idOfTask);
        historyManager.addTaskInHistory(task);
        return task;
    }

    @Override
    public Epic getEpic(int idOfTask) {
        Epic epic = epicCollection.get(idOfTask);
        historyManager.addTaskInHistory(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int idOfTask) {
        Subtask subtask = subtaskCollection.get(idOfTask);
        historyManager.addTaskInHistory(subtask);
        return subtask;
    }

    @Override
    public void removeTask(int idOfTask) {
        taskCollection.remove(idOfTask);
    }

    @Override
    public void removeEpic(int idOfTask) {
        for (int keyOfSubtask : epicCollection.get(idOfTask).getSubtasksIds()) {
            subtaskCollection.remove(keyOfSubtask);
        }
        epicCollection.remove(idOfTask);
    }

    @Override
    public void removeSubtask(int idOfTask) {
        int idOfEpic = subtaskCollection.get(idOfTask).getEpicId();
        subtaskCollection.remove(idOfTask);
        if (epicCollection.containsKey(idOfEpic)) {
            epicCollection.get(idOfEpic).removeSubtaskId(idOfTask);
            calculateEpicStatus(epicCollection.get(idOfEpic));
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>(epic.getSubtasksIds().size());
        for (Integer keySubtask : epic.getSubtasksIds()) {
            subtasksOfEpic.add(subtaskCollection.get(keySubtask));
        }
        return subtasksOfEpic;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}