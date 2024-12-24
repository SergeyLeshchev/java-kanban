package httphandler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import data.Endpoint;
import exceptions.NotFoundException;
import manager.TaskManager;

import java.io.IOException;

public class HistoryHttpHandler extends BaseHttpHandler {
    public HistoryHttpHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange);
        try {
            switch (endpoint) {
                case GET_HISTORY -> writeResponse(exchange, gson.toJson(taskManager.getHistory()), 200);
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
        if (request.equals("GET history")) {
            return Endpoint.GET_HISTORY;
        }
        return Endpoint.UNKNOWN;
    }
}
