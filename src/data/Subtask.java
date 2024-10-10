package data;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);
        this.epicId = epicID;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}