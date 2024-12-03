package manager;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import exceptions.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File savedTasks;

    public static void main(String[] args) {
        File taskStorage = null;
        try {
            taskStorage = new File("taskStorage.txt");
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
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в чтении или записи задач");
        } finally {
            assert taskStorage != null;
            taskStorage.delete();
        }
    }

    public FileBackedTaskManager(File savedTasks) throws IOException {
        this.savedTasks = savedTasks;
    }

    public File getSavedTasks() {
        return this.savedTasks;
    }

    private static String toStringTask(Task task) {
        String taskString = task.getIdOfTask() + ",";
        if (task.getClass().toString().endsWith("Task")) {
            taskString += "TASK,";
        } else if (task.getClass().toString().endsWith("Epic")) {
            taskString += "EPIC,";
        } else {
            taskString += "SUBTASK,";
        }
        taskString += task.getTitle() + "," + task.getStatus() + "," + task.getDescription() + ",";
        if (task.getClass().toString().endsWith("Subtask")) {
            Subtask subtask = (Subtask) task;
            taskString += subtask.getEpicId();
        }
        return taskString;
    }

    private Task fromString(String value) {
        String[] taskArray = value.split(",");
        Task task = null;
        switch (taskArray[1]) {
            case "TASK" -> task = new Task(taskArray[2], taskArray[4], Status.valueOf(taskArray[3]),
                        Integer.parseInt(taskArray[0]));
            case "EPIC" -> task = new Epic(taskArray[2], taskArray[4], Status.valueOf(taskArray[3]),
                        Integer.parseInt(taskArray[0]));
            case "SUBTASK" -> task = new Subtask(taskArray[2], taskArray[4], Status.valueOf(taskArray[3]),
                        Integer.parseInt(taskArray[5]), Integer.parseInt(taskArray[0]));
        }
        return task;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file.getName(), StandardCharsets.UTF_8))) {
            reader.readLine(); //считываем первую строку, чтобы она не пошла в цикл по созданию задач
            while (reader.ready()) {
                Task task = fileBackedTaskManager.fromString(reader.readLine());
                if (fileBackedTaskManager.idOfTasks < task.getIdOfTask()) {
                    fileBackedTaskManager.idOfTasks = task.getIdOfTask();
                }
                if (task.getClass().toString().endsWith("Task")) {
                    fileBackedTaskManager.taskCollection.put(task.getIdOfTask(), task);
                } else if (task.getClass().toString().endsWith("Epic")) {
                    fileBackedTaskManager.epicCollection.put(task.getIdOfTask(), (Epic) task);
                } else {
                    fileBackedTaskManager.subtaskCollection.put(task.getIdOfTask(), (Subtask) task);
                    fileBackedTaskManager.epicCollection.get(((Subtask) task).getEpicId()).addSubtaskId(task.getIdOfTask());
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
                writer.write(FileBackedTaskManager.toStringTask(task) + "\n");
            }
            for (Epic epic : epicCollection.values()) {
                writer.write(FileBackedTaskManager.toStringTask(epic) + "\n");
            }
            for (Subtask subtask : subtaskCollection.values()) {
                writer.write(FileBackedTaskManager.toStringTask(subtask) + "\n");
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

    void calculateEpicStatus(Epic epic) {
        super.calculateEpicStatus(epic);
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
