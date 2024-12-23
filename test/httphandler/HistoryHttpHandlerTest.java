package httphandler;

import com.google.gson.reflect.TypeToken;
import data.Epic;
import data.Status;
import data.Subtask;
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

class HistoryHttpHandlerTest {
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
    void getHistoryHandleTest() throws IOException, InterruptedException {
        Task task = new Task("task", "task description", Status.NEW,
                LocalDateTime.parse("2020-01-02T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task);
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("subtask", "subtask description", Status.NEW,
                LocalDateTime.parse("2001-12-16T21:21:21"), Duration.ofMinutes(10), 2);
        taskManager.addSubtask(subtask);
        taskManager.getSubtask(3);
        taskManager.getTask(1);
        taskManager.getEpic(2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type taskListType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> history = taskServer.getGson().fromJson(response.body(), taskListType);
        assertEquals(taskManager.getHistory(), history, "getHistoryHandleTest работает неправильно");
    }
}
