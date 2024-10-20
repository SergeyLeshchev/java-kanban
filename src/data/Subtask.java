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

    // Этот метод написан для альтернативной реализации
    public Subtask copy(Subtask subtask) {
        return new Subtask(subtask.getTitle(), subtask.getDescription(), subtask.getStatus(), subtask.epicId, subtask.getIdOfTask());
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}