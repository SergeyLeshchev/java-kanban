package httphandler;

import com.google.gson.reflect.TypeToken;
import data.Status;
import data.Task;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskHttpHandlerTest {
    HttpTaskServer taskServer;
    TaskManager taskManager;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = Managers.getDefaultTaskManager();
        taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
    }

    @AfterEach
    void shutDown() {
        taskServer.stop();
    }

    @Test
    void getTaskHandleTest() throws IOException, InterruptedException {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2001-01-02T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task task1 = HttpTaskServer.getGson().fromJson(response.body(), Task.class);
        assertEquals("task", task1.getTitle(), "getTaskHandleTest работает неправильно");
    }

    @Test
    void getTasksHandleTest() throws IOException, InterruptedException {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2001-01-02T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        Task task1 = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.parse("2001-01-03T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "task2 description", Status.NEW,
                LocalDateTime.parse("2001-01-04T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type taskListType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> tasks = HttpTaskServer.getGson().fromJson(response.body(), taskListType);
        assertEquals(taskManager.getAllTasks(), tasks, "getTasksHandleTest работает неправильно");
    }

    @Test
    void postCreateTaskHandleTest() throws IOException, InterruptedException {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2001-01-02T21:21:21"), Duration.ofMinutes(10));
        String taskJson = HttpTaskServer.getGson().toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = taskManager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("task", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    void postUpdateTaskHandleTest() throws IOException, InterruptedException {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2001-01-03T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        Task task1 = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.parse("2001-01-02T21:21:21"), Duration.ofMinutes(10), 1);
        String task1Json = HttpTaskServer.getGson().toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(task1Json)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = taskManager.getAllTasks();

        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("task1", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    void deleteTaskHandleTest() throws IOException, InterruptedException {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2001-01-02T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        assertEquals(1, taskManager.getAllTasks().size(), "Задача не была добавлена");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getAllTasks().size(), "Задача не была удалена");
    }
}
