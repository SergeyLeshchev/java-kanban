package httphandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import data.Task;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;
    private TaskManager taskManager;
    private final Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public static void main(String[] args) {

    }

    public void start() {
        httpServer.createContext("/tasks", new TaskHttpHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicHttpHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHttpHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHttpHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHttpHandler(taskManager, gson));
        System.out.println("Сервер запущен");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
        System.out.println("Сервер остановлен");
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public Gson getGson() {
        return gson;
    }
}
