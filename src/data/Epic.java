package data;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String title, String description, Status status) {
        super(title, description, status);
    }

    public Epic(String title, String description, Status status, int idOfTask) {
        super(title, description, status, idOfTask);
    }

    public Epic(String title, String description, Status status, ArrayList<Integer> subtasksIds, int idOfTask) {
        super(title, description, status, idOfTask);
        this.subtasksIds = subtasksIds;
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    public void addSubtaskId(int idOfSubtask) {
        subtasksIds.add(idOfSubtask);
    }

    public void removeSubtaskId(Integer idOfSubtask) {
        subtasksIds.remove(idOfSubtask);
    }
}