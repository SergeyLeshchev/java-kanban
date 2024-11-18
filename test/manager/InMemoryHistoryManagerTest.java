package manager;

import data.Status;
import data.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        int capacity = 1000000;
        for (int i = 0; i < capacity; i++) {
            historyManager.addTaskInHistory(new Task("Title" + i, "Description" + i, Status.NEW, i));
        }
        assertEquals(capacity, historyManager.getHistory().size(), "incorrect history size");
    }

    @Test
    void removeTest() {
        for (int i = 0; i < 10; i++) {
            historyManager.addTaskInHistory(new Task("Title" + i, "Description" + i, Status.NEW, i));
        }
        // проверяем удаление из начала, середины и конца
        historyManager.remove(0);
        historyManager.remove(5);
        historyManager.remove(9);
        assertEquals(7, historyManager.getHistory().size(), "remove() is not work correctly");
    }

    @Test
    void linkLastTest() {
        for (int i = 0; i < 3; i++) {
            historyManager.addTaskInHistory(new Task("Title" + i, "Description" + i, Status.NEW, i));
        }
        Task task2 = new Task("Title2", "Description2", Status.NEW, 2);
        assertEquals(task2, historyManager.getHistory().get(2), "linkLast() is not work correctly");
    }

    @Test
    void notRepeatTest() {
        Task task1 = new Task("Title1", "Description1", Status.NEW, 1);
        Task task2 = new Task("Title2", "Description2", Status.NEW, 2);
        Task task3 = new Task("Title3", "Description3", Status.NEW, 1);
        historyManager.addTaskInHistory(task1);
        historyManager.addTaskInHistory(task2);
        historyManager.addTaskInHistory(task3);
        assertEquals(2, historyManager.getHistory().size(), "same task not deleted");
        assertNotEquals(historyManager.getHistory().get(0), historyManager.getHistory().get(1),
                "same task not deleted");
    }
}
