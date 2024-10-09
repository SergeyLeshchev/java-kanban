package Data;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtaskID(int idOfSubtask) {
        subtasksIds.add(idOfSubtask);
    }

    public void removeSubtaskID(int idOfSubtask) {
        subtasksIds.remove((Integer) idOfSubtask);
    }
}