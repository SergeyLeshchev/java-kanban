package http_handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import data.Endpoint;
import exceptions.NotFoundException;
import manager.TaskManager;

import java.io.IOException;

public class EpicHttpHandler extends BaseHttpHandler implements HttpHandler {
    public EpicHttpHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        try {
            switch (endpoint) {
                case GET_EPIC -> writeResponse(exchange, gson.toJson(taskManager.getEpic(getId(exchange))), 200);
                case GET_EPICS -> writeResponse(exchange, gson.toJson(taskManager.getAllEpics()), 200);
                case GET_EPIC_SUBTASKS -> writeResponse(exchange,
                        gson.toJson(taskManager.getSubtasksOfEpic(taskManager.getEpic(getId(exchange)))), 200);
                case POST_CREATE_EPIC -> {
                    taskManager.addEpic(epic);
                    writeResponse(exchange, "Эпик создан успешно", 201);
                }
                case DELETE_EPIC -> {
                    taskManager.removeEpic(getId(exchange));
                    writeResponse(exchange, "Эпик удален успешно", 200);
                }
                default -> writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        } catch (NotFoundException e) {
            writeResponse(exchange, e.getMessage(), 404);
        } catch (IOException e) {
            writeResponse(exchange, "Произошла ошибка при обработке запроса", 500);
        }
    }
}
