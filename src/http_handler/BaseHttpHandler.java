package http_handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.Endpoint;
import data.Epic;
import data.Subtask;
import data.Task;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {
    protected TaskManager taskManager;
    Gson gson;
    Task task;
    Epic epic;
    Subtask subtask;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }

    public Endpoint getEndpoint(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        String request = exchange.getRequestMethod() + " " + pathParts[1];
        // Если запрос на добавление или обновление задачи, то создадим объект задачи из тела запроса
        switch (request) {
            case "POST tasks" -> task = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Task.class);
            case "POST subtasks" ->
                    subtask = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Subtask.class);
            case "POST epics" -> epic = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Epic.class);
        }
        // Если массив из 4 элементов, то это только GET_EPIC_SUBTASKS, вынес сюда для облегчения switch case
        if (pathParts.length == 4) {
            return Endpoint.GET_EPIC_SUBTASKS;
        }
        return switch (request) {
            case "GET tasks" -> (pathParts.length == 2) ? Endpoint.GET_TASKS : Endpoint.GET_TASK;
            case "POST tasks" -> (task.getIdOfTask() == 0) ? Endpoint.POST_CREATE_TASK : Endpoint.POST_UPDATE_TASK;
            case "DELETE tasks" -> Endpoint.DELETE_TASK;
            case "GET epics" -> (pathParts.length == 2) ? Endpoint.GET_EPICS : Endpoint.GET_EPIC;
            case "POST epics" -> Endpoint.POST_CREATE_EPIC;
            case "DELETE epics" -> Endpoint.DELETE_EPIC;
            case "GET subtasks" -> (pathParts.length == 2) ? Endpoint.GET_SUBTASKS : Endpoint.GET_SUBTASK;
            case "POST subtasks" ->
                    (subtask.getIdOfTask() == 0) ? Endpoint.POST_CREATE_SUBTASK : Endpoint.POST_UPDATE_SUBTASK;
            case "DELETE subtasks" -> Endpoint.DELETE_SUBTASK;
            case "GET history" -> Endpoint.GET_HISTORY;
            case "GET prioritized" -> Endpoint.GET_PRIORITIZED;
            default -> Endpoint.UNKNOWN;
        };
    }

    public int getId(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        return Integer.parseInt(pathParts[2]);
    }

    public void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Accept", "application/json");
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(StandardCharsets.UTF_8));
        }
        exchange.close();
    }
}
