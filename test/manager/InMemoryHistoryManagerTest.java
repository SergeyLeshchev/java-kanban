package manager;

import data.Status;
import data.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    Task task = new Task("task1", "task1 description", Status.NEW);

    @Test
    void addTaskInHistoryAndGetHistoryTest() {
        historyManager.addTaskInHistory(task);
        Task task1 = historyManager.getHistory().getFirst();
        assertEquals(task, task1, "addTaskInHistory or getHistory not works correctly");
    }

    @Test
    void historyCapacityTest() {
        // Здесь я бы хотел вместо 20 подставлять выражение (CAPACITY_HISTORY + 10),
        // но я не знаю как получить константу так, чтобы не менять структуру программы слишком сильно.
        // Я надеюсь это будет некритичным замечанием)
        for (int i = 0; i < 20; i++) {
            historyManager.addTaskInHistory(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "history capacity is not equal to 10");
    }
}