package manager;

import data.Status;
import data.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultTest() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        taskManager.addTask(task1);
        taskManager.getTask(1);

        taskManager.getHistory();
        assertEquals(task1, taskManager.getHistory().getFirst(), "getDefault() not works correctly");
    }
}