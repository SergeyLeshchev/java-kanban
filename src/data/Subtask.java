package data;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status, int epicId, int idOfTask) {
        super(title, description, status, idOfTask);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", idOfTask=" + super.getIdOfTask() +
                ", status=" + super.getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
