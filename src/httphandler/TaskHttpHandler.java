package httphandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import data.Endpoint;
import data.Task;
import exceptions.HasInteractionsException;
import exceptions.NotFoundException;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;

public class TaskHttpHandler extends BaseHttpHandler {
    private Task task;

    public TaskHttpHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("POST")) {
            task = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Task.class);
        }
        Endpoint endpoint = getEndpoint(exchange);
        try {
            switch (endpoint) {
                case GET_TASK -> writeResponse(exchange, gson.toJson(taskManager.getTask(getId(exchange))), 200);
                case GET_TASKS -> writeResponse(exchange, gson.toJson(taskManager.getAllTasks()), 200);
                case POST_CREATE_TASK -> {
                    taskManager.addTask(task);
                    writeResponse(exchange, "Задача создана успешно", 201);
                }
                case POST_UPDATE_TASK -> {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Задача обновлена успешно", 201);
                }
                case DELETE_TASK -> {
                    taskManager.removeTask(getId(exchange));
                    writeResponse(exchange, "Задача удалена успешно", 200);
                }
                default -> writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        } catch (NotFoundException e) {
            writeResponse(exchange, e.getMessage(), 404);
        } catch (HasInteractionsException e) {
            writeResponse(exchange, e.getMessage(), 406);
        } catch (IOException e) {
            writeResponse(exchange, "Произошла ошибка при обработке запроса", 500);
        }
    }

    private Endpoint getEndpoint(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        String request = exchange.getRequestMethod() + " " + pathParts[1];
        return switch (request) {
            case "GET tasks" -> (pathParts.length == 2) ? Endpoint.GET_TASKS : Endpoint.GET_TASK;
            case "POST tasks" -> (task.getIdOfTask() == 0) ? Endpoint.POST_CREATE_TASK : Endpoint.POST_UPDATE_TASK;
            case "DELETE tasks" -> Endpoint.DELETE_TASK;
            default -> Endpoint.UNKNOWN;
        };
    }
}
