package manager;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void addTaskAndGetTaskTest() {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2000-12-15T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTask(1), "addTask() or getTask() are not work correctly");
    }

    @Test
    void addEpicAndGetEpicTest() {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.getEpic(1), "addEpic() or getEpic() are not work correctly");
    }

    @Test
    void addSubtaskAndGetSubtaskTest() {
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2000-12-16T21:21:21"), Duration.ofMinutes(10), 5);
        taskManager.addSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtask(1), "addSubtask() or getSubtask() are not work correctly");
    }

    @Test
    void updateTaskTest() {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2000-12-17T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        Task task2 = new Task("task2", "task2 description", Status.DONE,
                LocalDateTime.parse("2000-12-18T21:21:21"), Duration.ofMinutes(10), 1);
        taskManager.updateTask(task2);
        assertEquals(task2.getTitle(), taskManager.getTask(1).getTitle(),
                "Title is not equal, updateTask() not works correctly");
        assertEquals(task2.getDescription(), taskManager.getTask(1).getDescription(),
                "Description is not equal, updateTask() not works correctly");
        assertEquals(task2.getStatus(), taskManager.getTask(1).getStatus(),
                "Status is not equal, updateTask() not works correctly");
        assertEquals(task2.getIdOfTask(), taskManager.getTask(1).getIdOfTask(),
                "IdOfTask is not equal, updateTask() not works correctly");
    }

    @Test
    void updateSubtaskTest() {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2000-12-19T21:21:21"), Duration.ofMinutes(10), epic.getIdOfTask());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.DONE,
                LocalDateTime.parse("2000-12-20T21:21:21"), Duration.ofMinutes(10), epic.getIdOfTask(), 2);
        taskManager.updateSubtask(subtask2);
        assertEquals(subtask2.getTitle(), taskManager.getSubtask(2).getTitle(),
                "Title is not equal, updateSubtask() not works correctly");
        assertEquals(subtask2.getDescription(), taskManager.getSubtask(2).getDescription(),
                "Description is not equal, updateSubtask() not works correctly");
        assertEquals(subtask2.getStatus(), taskManager.getSubtask(2).getStatus(),
                "Status is not equal, updateSubtask() not works correctly");
        assertEquals(subtask2.getEpicId(), taskManager.getSubtask(2).getEpicId(),
                "epicId is not equal, updateSubtask() not works correctly");
        assertEquals(subtask2.getIdOfTask(), taskManager.getSubtask(2).getIdOfTask(),
                "IdOfTask is not equal, updateSubtask() not works correctly");
    }

    @Test
    void updateEpicTest() {
        ArrayList<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW,
                LocalDateTime.parse("2000-12-21T21:21:21"), Duration.ofMinutes(10), list1, 1);
        taskManager.addEpic(epic1);
        ArrayList<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        Epic epic2 = new Epic("epic2", "epic2 description", Status.DONE,
                LocalDateTime.parse("2000-12-22T21:21:21"), Duration.ofMinutes(10), list2, 1);
        taskManager.updateEpic(epic2);
        assertEquals(epic2.getTitle(), taskManager.getEpic(1).getTitle(),
                "Title is not equal, updateEpic() not works correctly");
        assertEquals(epic2.getDescription(), taskManager.getEpic(1).getDescription(),
                "Description is not equal, updateEpic() not works correctly");
        assertEquals(epic2.getStatus(), taskManager.getEpic(1).getStatus(),
                "Status is not equal, updateEpic() not works correctly");
        assertEquals(epic2.getSubtasksIds(), taskManager.getEpic(1).getSubtasksIds(),
                "SubtasksIds is not equal, updateEpic() not works correctly");
        assertEquals(epic2.getIdOfTask(), taskManager.getEpic(1).getIdOfTask(),
                "IdOfTask is not equal, updateEpic() not works correctly");
    }

    @Test
    void getAllTasksTest() {
        Task task1 = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2000-11-13T21:21:21"), Duration.ofMinutes(10));
        Task task2 = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2000-11-14T21:21:21"), Duration.ofMinutes(10));
        Task task3 = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2000-11-15T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        assertEquals(3, taskManager.getAllTasks().size(), "getAllTasks() return ArrayList not right size");
    }

    @Test
    void getAllEpicsTest() {
        Epic epic1 = new Epic("epic", "epic description", Status.NEW);
        Epic epic2 = new Epic("epic", "epic description", Status.NEW);
        Epic epic3 = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        assertEquals(3, taskManager.getAllEpics().size(), "getAllEpics() return ArrayList not right size");
    }

    @Test
    void getAllSubtasksTest() {
        Subtask subtask1 = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2000-11-10T21:21:21"), Duration.ofMinutes(10), 5);
        Subtask subtask2 = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2000-11-11T21:21:21"), Duration.ofMinutes(10), 5);
        Subtask subtask3 = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2000-11-12T21:21:21"), Duration.ofMinutes(10), 5);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        assertEquals(3, taskManager.getAllSubtasks().size(), "getAllSubtasks() return ArrayList not right size");
    }

    @Test
    void removeAllTasksTest() {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2000-12-25T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        Task task2 = new Task("task2", "task2 description", Status.DONE,
                LocalDateTime.parse("2000-12-26T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task2);
        taskManager.removeAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "removeAllTasks() did not delete all tasks");
    }

    @Test
    void removeAllEpicsTest() {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        Epic epic2 = new Epic("epic2", "epic2 description", Status.DONE);
        taskManager.addEpic(epic2);
        taskManager.removeAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "removeAllEpics() did not delete all epics");
    }

    @Test
    void removeAllSubtasksTest() {
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2000-12-27T21:21:21"), Duration.ofMinutes(10), 5);
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.NEW,
                LocalDateTime.parse("2000-12-28T21:21:21"), Duration.ofMinutes(10), 5);
        taskManager.addSubtask(subtask2);
        taskManager.removeAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "removeAllSubtasks() did not delete all subtasks");
    }

    @Test
    void removeTaskTest() {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2000-12-29T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        assertEquals(taskManager.getTask(1), task);
        taskManager.removeTask(1);
        assertTrue(taskManager.getAllTasks().isEmpty(), "removeTask() did not delete task");
    }

    @Test
    void removeEpicTest() {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        assertEquals(taskManager.getEpic(1), epic);
        taskManager.removeEpic(1);
        assertTrue(taskManager.getAllEpics().isEmpty(), "removeEpic() did not delete epic");
    }

    @Test
    void removeSubtaskTest() {
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2000-12-30T21:21:21"), Duration.ofMinutes(10), 5);
        taskManager.addSubtask(subtask);
        assertEquals(taskManager.getSubtask(1), subtask);
        taskManager.removeSubtask(1);
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "removeSubtask() did not delete subtask");
    }

    @Test
    void getSubtasksOfEpicTest() {
        // Создадим 2 эпика с 2 сабтасками в каждом, чтобы быть уверенными, что метод вернет только нужные сабтаски
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW,
                LocalDateTime.parse("2000-11-01T21:21:21"), Duration.ofMinutes(10), epic.getIdOfTask());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.NEW,
                LocalDateTime.parse("2000-11-02T21:21:21"), Duration.ofMinutes(10), epic.getIdOfTask());
        taskManager.addSubtask(subtask2);
        Epic epic2 = new Epic("epic2", "epic2 description", Status.DONE);
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("subtask3", "subtask3 description", Status.NEW,
                LocalDateTime.parse("2000-11-03T21:21:21"), Duration.ofMinutes(10), epic2.getIdOfTask());
        taskManager.addSubtask(subtask3);
        Subtask subtask4 = new Subtask("subtask4", "subtask4 description", Status.NEW,
                LocalDateTime.parse("2000-11-04T21:21:21"), Duration.ofMinutes(10), epic2.getIdOfTask());
        taskManager.addSubtask(subtask4);

        boolean condition = (subtask3.equals(taskManager.getSubtasksOfEpic(epic2).get(0)) &&
                subtask4.equals(taskManager.getSubtasksOfEpic(epic2).get(1)));
        assertTrue(condition, "getSubtasksOfEpic() not work correctly");
    }

    @Test
    void getHistoryTest() {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2000-11-05T21:21:21"), Duration.ofMinutes(10));
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2000-11-06T21:21:21"), Duration.ofMinutes(10), epic.getIdOfTask());
        taskManager.addTask(task);
        taskManager.getTask(1);
        taskManager.addEpic(epic);
        taskManager.getEpic(2);
        taskManager.addSubtask(subtask);
        taskManager.getSubtask(3);

        boolean condition = (taskManager.getHistory().get(0).equals(task) &&
                taskManager.getHistory().get(1).equals(epic) &&
                taskManager.getHistory().get(2).equals(subtask));
        assertTrue(condition, "getHistoryTest() not work correctly");
    }
}
