package manager;

import data.Status;
import data.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AdditionalTest {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    @Test
    void TasksWithSetIdAndGenerateIdNotConflictTest() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        Task task2 = new Task("task2", "task2 description", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10), 1);
        taskManager.addTask(task1);
        assertEquals(taskManager.getTask(1), task2, "Id generate not correctly");
    }

    @Test
    void taskImmutabilityAfterAddingInManagerTest() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        taskManager.addTask(task1);
        assertEquals(task1.getTitle(), taskManager.getTask(1).getTitle(), "Title is not equal");
        assertEquals(task1.getDescription(), taskManager.getTask(1).getDescription(), "Description is not equal");
        assertEquals(task1.getStatus(), taskManager.getTask(1).getStatus(), "Status is not equal");
        assertEquals(task1.getIdOfTask(), taskManager.getTask(1).getIdOfTask(), "IdOfTask is not equal");
    }
}
