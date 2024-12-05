package manager;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import exceptions.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File savedTasks;

    public static void main(String[] args) {
        File taskStorage = new File("taskStorage.txt");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(taskStorage);
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        taskManager.addTask(task1);
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getIdOfTask());
        taskManager.addSubtask(subtask1);

        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(taskStorage);
        System.out.println(taskManager2.getTask(1));
        System.out.println(taskManager2.getEpic(2));
        System.out.println(taskManager2.getSubtask(3));
    }

    public FileBackedTaskManager(File savedTasks) {
        this.savedTasks = savedTasks;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getName(), StandardCharsets.UTF_8))) {
            reader.readLine(); //считываем первую строку, чтобы она не пошла в цикл по созданию задач
            while (reader.ready()) {
                Task task = TaskSerializer.fromString(reader.readLine());
                if (fileBackedTaskManager.idOfTasks < task.getIdOfTask()) {
                    fileBackedTaskManager.idOfTasks = task.getIdOfTask();
                }
                switch (task.getType()) {
                    case TASK -> fileBackedTaskManager.taskCollection.put(task.getIdOfTask(), task);
                    case EPIC -> fileBackedTaskManager.epicCollection.put(task.getIdOfTask(), (Epic) task);
                    case SUBTASK -> {
                        fileBackedTaskManager.subtaskCollection.put(task.getIdOfTask(), (Subtask) task);
                        fileBackedTaskManager.epicCollection.get(((Subtask) task).getEpicId()).
                                addSubtaskId(task.getIdOfTask());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.");
        }
        return fileBackedTaskManager;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedTasks, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : taskCollection.values()) {
                writer.write(TaskSerializer.toStringTask(task) + "\n");
            }
            for (Epic epic : epicCollection.values()) {
                writer.write(TaskSerializer.toStringTask(epic) + "\n");
            }
            for (Subtask subtask : subtaskCollection.values()) {
                writer.write(TaskSerializer.toStringTask(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи задачи в файл.");
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeTask(int idOfTask) {
        super.removeTask(idOfTask);
        save();
    }

    @Override
    public void removeEpic(int idOfTask) {
        super.removeEpic(idOfTask);
        save();
    }

    @Override
    public void removeSubtask(int idOfTask) {
        super.removeSubtask(idOfTask);
        save();
    }
}
