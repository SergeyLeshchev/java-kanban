package httphandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import data.Endpoint;
import data.Epic;
import exceptions.NotFoundException;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;

public class EpicHttpHandler extends BaseHttpHandler {
    private Epic epic;

    public EpicHttpHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("POST")) {
            epic = gson.fromJson(new InputStreamReader(exchange.getRequestBody()), Epic.class);
        }
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

    private Endpoint getEndpoint(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        String request = exchange.getRequestMethod() + " " + pathParts[1];
        if (pathParts.length == 4) {
            return Endpoint.GET_EPIC_SUBTASKS;
        }
        return switch (request) {
            case "GET epics" -> (pathParts.length == 2) ? Endpoint.GET_EPICS : Endpoint.GET_EPIC;
            case "POST epics" -> Endpoint.POST_CREATE_EPIC;
            case "DELETE epics" -> Endpoint.DELETE_EPIC;
            default -> Endpoint.UNKNOWN;
        };
    }
}
