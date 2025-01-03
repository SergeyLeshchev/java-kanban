package manager;

import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import exceptions.ManagerSaveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File testFile;

    @BeforeEach
    void fileAndManagerGeneration() {
        try {
            testFile = Files.createFile(Paths.get("testFile.txt")).toFile();
            taskManager = new FileBackedTaskManager(testFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ManagerSaveException("Неправильно работает создание файла или файлового менеджера");
        }
    }

    @AfterEach
    void deleteFile() {
        if (testFile != null) {
            testFile.delete();
        }
    }

    @Test
    void saveAndLoadTasksTest() {
        Task task = new Task("Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.parse("2000-12-12T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW,
                LocalDateTime.parse("2000-12-13T21:21:21"), Duration.ofMinutes(10), epic.getIdOfTask());
        taskManager.addSubtask(subtask);
        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(testFile);
        assertEquals(taskManager.getAllTasks(), taskManager2.getAllTasks(), "Tasks are not equals");
        assertEquals(taskManager.getAllEpics(), taskManager2.getAllEpics(), "Epics are not equals");
        assertEquals(taskManager.getAllSubtasks(), taskManager2.getAllSubtasks(), "Subtasks are not equals");
    }

    @Test
    void exceptionSaveTest() {
        Assertions.assertDoesNotThrow(() -> {
            taskManager = FileBackedTaskManager.loadFromFile(testFile);
        }, "Exception thrown");
    }
}
