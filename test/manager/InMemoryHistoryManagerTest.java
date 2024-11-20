package manager;

import data.Status;
import data.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task1;
    Task task2;

    @BeforeEach
    void makeTasks() {
        historyManager = Managers.getDefaultHistoryManager();
        task1 = new Task("task1", "task1 description", Status.NEW, 1);
        task2 = new Task("task2", "task2 description", Status.NEW, 2);
    }

    @Test
    void addTaskInHistoryAndGetHistoryAndLinkLastTest() {
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task2);
        assertEquals(task2, historyManager.getHistory().get(1), "addTaskInHistory or getHistory or LinkLast not works correctly");
    }

    @Test
    void removeTest() {
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task2);
        historyManager.addTaskInHistory(new Task("task3", "task3 description", Status.NEW, 3));
        historyManager.addTaskInHistory(new Task("task4", "task4 description", Status.NEW, 4));
        historyManager.remove(3); // проверяем удаление из середины
        historyManager.remove(4); // проверяем удаление с конца
        historyManager.remove(1); // проверяем удаление с начала
        historyManager.remove(2); // проверяем удаление единственного элемента
        assertEquals(0, historyManager.getHistory().size(), "remove() is not work correctly");
    }

    @Test
    void notRepeatTest() {
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task2);
        historyManager.addTaskInHistory(task1);
        assertEquals(2, historyManager.getHistory().size(), "same task not deleted");
        assertNotEquals(historyManager.getHistory().get(0), historyManager.getHistory().get(1), "same task not deleted");
    }
}
