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
        taskCollection.put(task.getIdOfTask(), task);
    }

    public void addEpic(Epic epic) {
        epic.setIdOfTask(idOfTasks);
        epicCollection.put(idOfTasks, epic);
        idOfTasks++;
    }

    public void updateEpic(Epic epic) {
        epicCollection.put(epic.getIdOfTask(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setIdOfTask(idOfTasks);
        subtaskCollection.put(idOfTasks, subtask);
        idOfTasks++;

        Epic epic = epicCollection.get(subtask.getEpicId());
        epic.subtasksIds.add(subtask.getIdOfTask());
        calculateEpicStatus(epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtaskCollection.put(subtask.getIdOfTask(), subtask);
        calculateEpicStatus(epicCollection.get(subtask.getEpicId()));
    }

    public void calculateEpicStatus(Epic epic) {
        // если подзадач нет, то статус epic - NEW
        if (epic.subtasksIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        // если хоть одна подзадача IN_PROGRESS, то статус epic - IN_PROGRESS
        for (Integer idOfSubtask : epic.subtasksIds) {
            if (subtaskCollection.get(idOfSubtask).getStatus() == Status.IN_PROGRESS) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        // если нет подзадач со статусом DONE, то статус epic - NEW
        Status status = Status.NEW;
        for (Integer idOfSubtask : epic.subtasksIds) {
            if (subtaskCollection.get(idOfSubtask).getStatus() == Status.DONE) {
                status = Status.DONE;
            }
        }
        if (status == Status.NEW) {
            epic.setStatus(Status.NEW);
            return;
        }
        // если не все подзадачи DONE, то статус epic - IN_PROGRESS
        for (Integer idOfSubtask : epic.subtasksIds) {
            if (subtaskCollection.get(idOfSubtask).getStatus() != Status.DONE) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        epic.setStatus(Status.DONE);
    }

    public String getAllTasks() {
        String allTasks = "Вот список обычных задач:\n";
        for (Integer key : taskCollection.keySet()) {
            allTasks += taskCollection.get(key).getTitle() + "\n";
        }
        allTasks += "Вот список epic-задач с их подзадачами:\n";
        int counter;
        for (Integer keyEpic : epicCollection.keySet()) {
            allTasks += epicCollection.get(keyEpic).getTitle() + "\n";
            counter = 1;
            for (Integer keySubtask : epicCollection.get(keyEpic).subtasksIds) {
                allTasks += counter++ + ". " + subtaskCollection.get(keySubtask).getTitle() + "\n";
            }
        }
        return allTasks;
    }

    public void deleteAllTasks() {
        taskCollection.clear();
        epicCollection.clear();
        subtaskCollection.clear();
        idOfTasks = 1;
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
        for (int keyOfSubtask : epicCollection.get(idOfTask).subtasksIds) {
            subtaskCollection.remove(keyOfSubtask);
        }
        epicCollection.remove(idOfTask);
    }

    public void removeSubtask(int idOfTask) {
        int idOfEpic = subtaskCollection.get(idOfTask).getEpicId();
        subtaskCollection.remove(idOfTask);
        epicCollection.get(idOfEpic).subtasksIds.remove((Integer) idOfTask);
        calculateEpicStatus(epicCollection.get(idOfEpic));
    }

    public String subtasksOfEpic(Epic epic) {
        String subtasksOfEpic = "";
        int counter = 1;
        for (Integer keySubtask : epic.subtasksIds) {
            subtasksOfEpic += counter++ + ". " + subtaskCollection.get(keySubtask).getTitle() + "\n";
        }
        return subtasksOfEpic;
    }
}