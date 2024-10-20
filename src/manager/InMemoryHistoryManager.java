package manager;

import data.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final int CAPACITY_HISTORY = 10;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addTaskInHistory(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() == CAPACITY_HISTORY) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}