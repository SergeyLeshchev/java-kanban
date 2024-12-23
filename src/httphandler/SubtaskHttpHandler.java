package httphandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.Endpoint;
import exceptions.HasInteractionsException;
import exceptions.NotFoundException;
import manager.TaskManager;

import java.io.IOException;

public class SubtaskHttpHandler extends BaseHttpHandler implements HttpHandler {
    public SubtaskHttpHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        try {
            switch (endpoint) {
                case GET_SUBTASK -> writeResponse(exchange, gson.toJson(taskManager.getSubtask(getId(exchange))), 200);
                case GET_SUBTASKS -> writeResponse(exchange, gson.toJson(taskManager.getAllSubtasks()), 200);
                case POST_CREATE_SUBTASK -> {
                    taskManager.addSubtask(subtask);
                    writeResponse(exchange, "Подзадача создана успешно", 201);
                }
                case POST_UPDATE_SUBTASK -> {
                    taskManager.updateSubtask(subtask);
                    writeResponse(exchange, "Подзадача обновлена успешно", 201);
                }
                case DELETE_SUBTASK -> {
                    taskManager.removeSubtask(getId(exchange));
                    writeResponse(exchange, "Подзадача удалена успешно", 200);
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
