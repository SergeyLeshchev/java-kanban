package http_handler;

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

class EpicHttpHandlerTest {
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
    void getEpicHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Epic epic1 = taskServer.getGson().fromJson(response.body(), Epic.class);
        assertEquals("epic", epic1.getTitle(), "getEpicHandleTest работает неправильно");
    }

    @Test
    void getEpicsHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2001-12-16T21:21:21"), Duration.ofMinutes(10), 2);
        taskManager.addSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type epicListType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        List<Epic> epics = taskServer.getGson().fromJson(response.body(), epicListType);
        assertEquals(taskManager.getAllEpics(), epics, "getEpicsHandleTest работает неправильно");
    }

    @Test
    void getEpicSubtasksHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW,
                LocalDateTime.parse("2099-12-16T21:21:21"), Duration.ofMinutes(10), 1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.NEW,
                LocalDateTime.parse("2099-12-17T21:21:21"), Duration.ofMinutes(10), 1);
        taskManager.addSubtask(subtask2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type subtaskListType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        List<Subtask> subtasks = taskServer.getGson().fromJson(response.body(), subtaskListType);
        assertEquals(taskManager.getSubtasksOfEpic(epic), subtasks, "getEpicSubtasksHandleTest работает неправильно");
    }

    @Test
    void postCreateEpicHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        String epicJson = taskServer.getGson().toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> epicsFromManager = taskManager.getAllEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("epic", epicsFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    void deleteEpicHandleTest() throws IOException, InterruptedException {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size(), "Эпик не был добавлен");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getAllEpics().size(), "Эпик не был удален");
    }
}
