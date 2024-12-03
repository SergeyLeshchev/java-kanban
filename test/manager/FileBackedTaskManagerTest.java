package manager;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import exceptions.ManagerSaveException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    @Test
    public void emptyFileGenerationTest() {
        try {
            FileBackedTaskManager taskManager = new FileBackedTaskManager(File.createTempFile("testFile", ".txt"));
            Task task = new Task("Задача 1", "Описание задачи 1", Status.NEW);
            taskManager.addTask(task);
            taskManager.removeTask(1);
            try (BufferedReader reader = new BufferedReader(new FileReader(taskManager.getSavedTasks(), StandardCharsets.UTF_8))) {
                reader.readLine(); //считываем первую строку, чтобы убрать заголовок
                assertFalse(reader.ready(), "File is not empty");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ManagerSaveException("Неправильно работает тест на создание пустого файла");
        }
    }

    @Test
    public void emptyFileLoadingTest() {
        File testFile = null;
        try {
            testFile = Files.createFile(Paths.get("testFile.txt")).toFile();
            FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(new File(testFile.getName()));
            assertNotNull(taskManager.getSavedTasks(), "file savedTasks is null");

        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ManagerSaveException("Неправильно работает тест на загрузку пустого файла");
        } finally {
            assert testFile != null;
            testFile.delete();
        }
    }

    @Test
    public void saveSomeTasksTest() {
        File testFile = null;
        try {
            testFile = Files.createFile(Paths.get("testFile.txt")).toFile();
            FileBackedTaskManager taskManager = new FileBackedTaskManager(File.createTempFile("testFile", ".txt"));
            Task task = new Task("Задача 1", "Описание задачи 1", Status.NEW);
            taskManager.addTask(task);
            Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
            taskManager.addEpic(epic);
            Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic.getIdOfTask());
            taskManager.addSubtask(subtask);
            try (BufferedReader reader = new BufferedReader(new FileReader(taskManager.getSavedTasks(), StandardCharsets.UTF_8))) {
                String correctOutput = "id,type,name,status,description,epic\n" +
                        "1,TASK,Задача 1,NEW,Описание задачи 1,\n" +
                        "2,EPIC,Эпик 1,NEW,Описание эпика 1,\n" +
                        "3,SUBTASK,Подзадача 1,NEW,Описание подзадачи 1,2\n";
                String testOutput = "";
                while (reader.ready()) {
                    testOutput += reader.readLine() + "\n";
                }
                assertEquals(correctOutput, testOutput, "saving tasks is not work correctly");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ManagerSaveException("Неправильно работает тест на загрузку пустого файла");
        } finally {
            assert testFile != null;
            testFile.delete();
        }
    }

    @Test
    public void loadSomeTasksTest() {
        File testFile = null;
        try {
            testFile = Files.createFile(Paths.get("testFile.txt")).toFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, StandardCharsets.UTF_8))) {
                writer.write("id,type,name,status,description,epic\n" +
                        "1,TASK,Задача 1,NEW,Описание задачи 1,\n" +
                        "2,EPIC,Эпик 1,NEW,Описание эпика 1,\n" +
                        "3,SUBTASK,Подзадача 1,NEW,Описание подзадачи 1,2\n");
            }
            FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(new File(testFile.getName()));
            String correctOutput = "Task{title='Задача 1', description='Описание задачи 1', idOfTask=1, status=NEW}" +
                    "Epic{title='Эпик 1', description='Описание эпика 1', idOfTask=2, status=NEW, subtasksIds=[3]}" +
                    "Subtask{title='Подзадача 1', description='Описание подзадачи 1', idOfTask=3, status=NEW, epicId=2}";
            String testOutput = taskManager.getTask(1).toString() +
                    taskManager.getEpic(2).toString() +
                    taskManager.getSubtask(3).toString();
            assertEquals(correctOutput, testOutput, "loading tasks is not work correctly");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ManagerSaveException("Неправильно работает тест на загрузку пустого файла");
        } finally {
            assert testFile != null;
            testFile.delete();
        }
    }
}
