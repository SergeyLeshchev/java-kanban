package httphandler;

import com.google.gson.reflect.TypeToken;
import data.Epic;
import data.Status;
import data.Subtask;
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

class SubtaskHttpHandlerTest {
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
    void getSubtaskHandleTest() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2099-12-16T21:21:21"), Duration.ofMinutes(10), 5);
        taskManager.addSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask subtask1 = HttpTaskServer.getGson().fromJson(response.body(), Subtask.class);
        assertEquals("subtask", subtask1.getTitle(), "getSubtaskHandleTest работает неправильно");
    }

    @Test
    void getSubtasksHandleTest() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2099-12-15T21:21:21"), Duration.ofMinutes(10), 5);
        taskManager.addSubtask(subtask);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW,
                LocalDateTime.parse("2099-12-16T21:21:21"), Duration.ofMinutes(10), 5);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.NEW,
                LocalDateTime.parse("2099-12-17T21:21:21"), Duration.ofMinutes(10), 5);
        taskManager.addSubtask(subtask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type subtaskListType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = HttpTaskServer.getGson().fromJson(response.body(), subtaskListType);
        assertEquals(taskManager.getAllSubtasks(), subtasks, "getSubtasksHandleTest работает неправильно");
    }

    @Test
    void postCreateSubtaskHandleTest() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2099-12-16T21:21:21"), Duration.ofMinutes(10), 5);
        String subtaskJson = HttpTaskServer.getGson().toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("subtask", subtasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    void postUpdateSubtaskHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2099-12-16T21:21:21"), Duration.ofMinutes(10), 1);
        taskManager.addSubtask(subtask);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW,
                LocalDateTime.parse("2099-12-17T21:21:21"), Duration.ofMinutes(10), 1, 2);
        String subtask1Json = HttpTaskServer.getGson().toJson(subtask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtask1Json)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();

        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("subtask1", subtasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    void deleteSubtaskHandleTest() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2099-12-17T21:21:21"), Duration.ofMinutes(10), 1, 2);
        taskManager.addSubtask(subtask);
        assertEquals(1, taskManager.getAllSubtasks().size(), "Подзадача не была добавлена");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getAllSubtasks().size(), "Подзадача не была удалена");
    }
}
