package manager;

import data.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final int CAPACITY_HISTORY = 10;
    private final List<Task> history = new ArrayList<>();
    // Можно раскомментировать эту часть, закомментировать остальной код и проверить как работает
    // альтернативная реализация
/*
    @Override
    public void addTaskInHistory(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() == CAPACITY_HISTORY) {
            history.removeFirst();
        }
        history.add(task.copy(task));
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
*/

    @Override
    public void addTaskInHistory(Task task) {
        history.add(task);
        if (history.size() > CAPACITY_HISTORY) {
            history.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }

}