package manager;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void generateTasksForTest() {
        super.taskManager = (InMemoryTaskManager) Managers.getDefaultTaskManager();
    }

    @Test
    void calculateEpicStatusTest() {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10), epic.getIdOfTask());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10), epic.getIdOfTask());
        taskManager.addSubtask(subtask2);
        assertEquals(Status.NEW, taskManager.getEpic(1).getStatus(),
                "epic status is not equal NEW, when all subtasks - NEW");

        taskManager.getSubtask(2).setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(1).getStatus(),
                "epic status is not equal IN_PROGRESS, when there are subtask - IN_PROGRESS");

        taskManager.getSubtask(2).setStatus(Status.DONE);
        taskManager.getSubtask(3).setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(Status.DONE, taskManager.getEpic(1).getStatus(),
                "epic status is not equal DONE, when all subtasks - DONE");

        taskManager.getSubtask(2).setStatus(Status.IN_PROGRESS);
        taskManager.removeSubtask(3);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpic(1).getStatus(),
                "epic status is not equal IN_PROGRESS, when there is subtask IN_PROGRESS after method removeSubtask()");

        taskManager.removeAllSubtasks();
        assertEquals(Status.NEW, taskManager.getEpic(1).getStatus(),
                "epic status is not equal NEW, when epic is empty");
    }

    @Test
    void subtaskHasEpicTest() {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10), epic.getIdOfTask());
        taskManager.addSubtask(subtask2);
        assertEquals(taskManager.getEpic(1), taskManager.getEpic(subtask2.getEpicId()),
                "subtask not has EpicId");
    }

    @Test
    void isTaskCrossTest() {
        Task task1 = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.parse("2020-12-21T01:00:00"), Duration.ofMinutes(10));
        Task task2 = new Task("task2", "task2 description", Status.NEW,
                LocalDateTime.parse("2020-12-21T01:05:00"), Duration.ofMinutes(10));
        Task task3 = new Task("task3", "task3 description", Status.NEW,
                LocalDateTime.parse("2020-12-21T00:55:00"), Duration.ofMinutes(10));
        Task task4 = new Task("task4", "task4 description", Status.NEW,
                LocalDateTime.parse("2020-12-21T00:40:00"), Duration.ofMinutes(10));
        Task task5 = new Task("task5", "task5 description", Status.NEW,
                LocalDateTime.parse("2020-12-21T01:20:00"), Duration.ofMinutes(10));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        taskManager.addTask(task5);
        assertEquals(taskManager.getPrioritizedTasks(), List.of(task4, task1, task5),
                "isTaskCross method works not right");
    }
}
