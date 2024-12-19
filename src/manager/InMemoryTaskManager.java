package manager;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static data.TaskType.SUBTASK;
import static data.TaskType.TASK;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    protected final HashMap<Integer, Task> taskCollection = new HashMap<>();
    protected final HashMap<Integer, Epic> epicCollection = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtaskCollection = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>();
    protected int idOfTasks = 1;

    @Override
    public void addTask(Task task) {
        task.setIdOfTask(idOfTasks);
        taskCollection.put(idOfTasks, task);

        if (isValidTime(task) && prioritizedTasks.stream()
                .noneMatch(task1 -> isTaskCross(task, task1))) {
            prioritizedTasks.add(task);
        }
        idOfTasks++;
    }

    @Override
    public void updateTask(Task task) {
        if (taskCollection.containsKey(task.getIdOfTask())) {
            taskCollection.put(task.getIdOfTask(), task);
        }

        if (isValidTime(task) && prioritizedTasks.stream()
                .noneMatch(task1 -> isTaskCross(task, task1))) {
            // удаляем старый экземпляр задачи, если в списке есть задача с этим же id
            if (prioritizedTasks.contains(task)) {
                prioritizedTasks.remove(task);
            }
            // добавляем обновленную задачу
            prioritizedTasks.add(task);
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
        if (isValidTime(subtask) && prioritizedTasks.stream()
                .noneMatch(subtask1 -> isTaskCross(subtask, subtask1))) {
            prioritizedTasks.add(subtask);
        }
        idOfTasks++;

        if (epicCollection.containsKey(subtask.getEpicId())) {
            Epic epic = epicCollection.get(subtask.getEpicId());
            if (epic != null) {
                epic.addSubtaskId(subtask.getIdOfTask());
                calculateEpicStatus(epic);
                calculateEpicTimeAndDuration(epic);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskCollection.containsKey(subtask.getIdOfTask())) {
            subtaskCollection.put(subtask.getIdOfTask(), subtask);
            if (isValidTime(subtask) && prioritizedTasks.stream()
                    .noneMatch(subtask1 -> isTaskCross(subtask, subtask1))) {
                // удаляем старый экземпляр задачи, если в списке есть задача с этим же id
                if (prioritizedTasks.contains(subtask)) {
                    prioritizedTasks.remove(subtask);
                }
                // добавляем обновленную задачу
                prioritizedTasks.add(subtask);
            }
            calculateEpicStatus(epicCollection.get(subtask.getEpicId()));
            calculateEpicTimeAndDuration(epicCollection.get(subtask.getEpicId()));
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

    private void calculateEpicTimeAndDuration(Epic epic) {
        Optional<LocalDateTime> startTimeOptional = epic.getSubtasksIds().stream()
                .map(subtaskId -> getSubtask(subtaskId).getStartTime())
                .min(Comparator.naturalOrder());
        epic.setStartTime(startTimeOptional.get());

        Optional<LocalDateTime> endTimeOptional = epic.getSubtasksIds().stream()
                .map(subtaskId -> getSubtask(subtaskId).getStartTime())
                .max(Comparator.naturalOrder());
        epic.setEndTime(endTimeOptional.get());

        Duration duration = Duration.ofMinutes(0);
        for (int subtaskId : epic.getSubtasksIds()) {
            duration = duration.plus(getSubtask(subtaskId).getDuration());
        }
        epic.setDuration(duration);
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
        taskCollection.keySet().forEach(idOfTask -> historyManager.remove(idOfTask));
        taskCollection.clear();
        prioritizedTasks.stream()
                .filter(task -> task.getType().equals(TASK))
                .map(prioritizedTasks::remove);
    }

    @Override
    public void removeAllEpics() {
        for (Integer idOfEpic : epicCollection.keySet()) {
            historyManager.remove(idOfEpic);
        }
        subtaskCollection.keySet().forEach(subtaskId -> {
            historyManager.remove(subtaskId);
            prioritizedTasks.remove(getSubtask(subtaskId));
        });
        subtaskCollection.clear();
        epicCollection.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subtaskCollection.keySet().forEach(historyManager::remove);
        subtaskCollection.clear();
        prioritizedTasks.stream()
                .filter(subtask -> subtask.getType().equals(SUBTASK))
                .map(prioritizedTasks::remove);
        epicCollection.values().forEach(epic -> {
            epic.getSubtasksIds().clear();
            epic.setStatus(Status.NEW);
        });
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
        prioritizedTasks.remove(taskCollection.get(idOfTask));
        taskCollection.remove(idOfTask);
        historyManager.remove(idOfTask);
    }

    @Override
    public void removeEpic(int idOfTask) {
        epicCollection.get(idOfTask).getSubtasksIds().forEach(subtaskId -> {
            historyManager.remove(subtaskCollection.get(subtaskId).getIdOfTask());
            prioritizedTasks.remove(subtaskCollection.get(subtaskId));
            subtaskCollection.remove(subtaskId);
        });
        epicCollection.remove(idOfTask);
        historyManager.remove(idOfTask);
    }

    @Override
    public void removeSubtask(int idOfTask) {
        prioritizedTasks.remove(subtaskCollection.get(idOfTask));
        int idOfEpic = subtaskCollection.get(idOfTask).getEpicId();
        subtaskCollection.remove(idOfTask);
        if (epicCollection.containsKey(idOfEpic)) {
            epicCollection.get(idOfEpic).removeSubtaskId(idOfTask);
            calculateEpicStatus(epicCollection.get(idOfEpic));
            calculateEpicTimeAndDuration(epicCollection.get(idOfEpic));
        }
        historyManager.remove(idOfTask);
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Epic epic) {
        return new ArrayList<>(epic.getSubtasksIds().stream()
                .map(subtaskId -> subtaskCollection.get(subtaskId))
                .collect(Collectors.toList()));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean isValidTime(Task task) {
        return task.getStartTime() != null;
    }

    private boolean isTaskCross(Task task1, Task task2) {
        return !(task1.getStartTime().isAfter(task2.getEndTime()) || task1.getEndTime().isBefore(task2.getStartTime()));
    }
}
