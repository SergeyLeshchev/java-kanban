package manager;

import data.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addTaskInHistory(Task task);

    void remove(int id);

    ArrayList<Task> getHistory();
}
