package httphandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.Endpoint;
import exceptions.HasInteractionsException;
import exceptions.NotFoundException;
import manager.TaskManager;

import java.io.IOException;

public class TaskHttpHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHttpHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
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
}
