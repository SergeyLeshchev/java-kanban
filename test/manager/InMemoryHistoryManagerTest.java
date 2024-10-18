package manager;

import data.Status;
import data.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager taskManager = Managers.getDefault();

    @Test
    void historySavedOldVersionOfTheTaskTest() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        taskManager.addTask(task1);
        taskManager.getTask(1);
        taskManager.getTask(1).setStatus(Status.IN_PROGRESS);
        taskManager.getHistory();
    }

}