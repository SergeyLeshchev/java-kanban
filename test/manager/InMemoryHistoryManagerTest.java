package manager;

import data.Status;
import data.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    void makeTasks() {
        historyManager = Managers.getDefaultHistoryManager();
        task1 = new Task("task1", "task1 description", Status.NEW, 1);
        task2 = new Task("task2", "task2 description", Status.NEW, 2);
        task3 = new Task("task3", "task3 description", Status.NEW, 3);
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task2);
        historyManager.addTaskInHistory(task3);
    }

    @Test
    void addTaskInHistoryAndGetHistoryAndLinkLastTest() {
        assertEquals(task3, historyManager.getHistory().get(2),
                "addTaskInHistory or getHistory or LinkLast not work correctly");
    }

    @Test
    void removeInStartTest() {
        historyManager.remove(1);
        assertEquals(List.of(task2, task3), historyManager.getHistory(), "remove() from start not works correctly");
    }

    @Test
    void removeInMiddleTest() {
        historyManager.remove(2);
        assertEquals(List.of(task1, task3), historyManager.getHistory(), "remove() from middle not works correctly");
    }

    @Test
    void removeInEndTest() {
        historyManager.remove(3);
        assertEquals(List.of(task1, task2), historyManager.getHistory(), "remove() from end not works correctly");
    }

    @Test
    void notRepeatTest() {
        historyManager.addTaskInHistory(task1);
        assertEquals(3, historyManager.getHistory().size(), "same task not deleted");
        assertNotEquals(historyManager.getHistory().get(0), historyManager.getHistory().get(2), "same task not deleted");
    }
}
