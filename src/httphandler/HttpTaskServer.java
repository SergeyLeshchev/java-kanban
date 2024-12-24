package httphandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;
    private final TaskManager taskManager;
    private static final Gson gson = createGson();

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.taskManager = taskManager;
    }

    public static void main(String[] args) {
        // Образец задач для использования в разработке
        TaskManager taskManager = Managers.getDefaultTaskManager();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10));
        taskManager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:20:21"), Duration.ofMinutes(10));
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW,
                LocalDateTime.parse("2002-12-21T21:21:21"), Duration.ofMinutes(10), epic1.getIdOfTask());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(20), epic1.getIdOfTask());
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", Status.NEW,
                LocalDateTime.parse("2001-12-21T21:21:21"), Duration.ofMinutes(10), epic1.getIdOfTask());
        taskManager.addSubtask(subtask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Status.NEW);
        taskManager.addEpic(epic2);

        // Пользовательский сценарий для InMemoryHistoryManager
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getSubtask(5);
        taskManager.getSubtask(6);
        taskManager.getEpic(7);

        // Вызовем некоторые задачи для создания повторов
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getEpic(7);

        // Задачи должны быть распечатаны в порядке id: 1, 5, 6, 2, 3, 4, 7
        System.out.println("История: Задачи должны быть распечатаны в порядке id: 1, 5, 6, 2, 3, 4, 7");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println("Задачи по приоритету:");
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }

        // Удалим одну задачу и эпик с тремя подзадачами. Выведем что получилось:
        taskManager.removeTask(1);
        taskManager.removeEpic(3);
        System.out.println("История после удаления: Задачи должны быть распечатаны в порядке id: 2, 7");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }

    public void start() {
        httpServer.createContext("/tasks", new TaskHttpHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicHttpHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHttpHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHttpHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHttpHandler(taskManager, gson));
        httpServer.start();
        System.out.println("Сервер запущен");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен");
    }

    public static Gson getGson() {
        return gson;
    }

    private static Gson createGson() {
        return new GsonBuilder()
                .serializeNulls()
                // Если закомментировать следующую строку с TaskAdapter, то не работает сериализация списка
                // истории и приоритетных задач в json и обратно
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
