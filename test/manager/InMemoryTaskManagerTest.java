package manager;

import data.Status;
import data.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void addTaskAndGetTaskTest() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        taskManager.addTask(task1);
        assertEquals(task1, taskManager.getTask(1), "addTask() or getTask() are not work correctly");
    }

    @Test
    void addEpicAndGetEpicTest() {
        Task epic1 = new Task("epic1", "epic1 description", Status.NEW);
        taskManager.addTask(epic1);
        assertEquals(epic1, taskManager.getTask(1), "addEpic() or getEpic() are not work correctly");
    }

    @Test
    void addSubtaskAndGetSubtaskTest() {
        Task subtask1 = new Task("subtask1", "subtask1 description", Status.NEW);
        taskManager.addTask(subtask1);
        assertEquals(subtask1, taskManager.getTask(1), "addSubtask() or getSubtask() are not work correctly");
    }

    @Test
    void TasksWithSetIdAndGenerateIdNotConflictTest() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        Task task2 = new Task("task2", "task2 description", Status.NEW, 1);
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