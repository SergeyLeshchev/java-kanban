package http_handler;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskAdapter implements JsonSerializer<Task>, JsonDeserializer<Task> {

    @Override
    public JsonElement serialize(Task task, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", task.getTitle());
        jsonObject.addProperty("description", task.getDescription());
        jsonObject.addProperty("status", task.getStatus().toString());
        jsonObject.addProperty("idOfTask", task.getIdOfTask());

        if (task.getStartTime() != null) {
            jsonObject.addProperty("startTime", task.getStartTime().toString());
        }

        if (task.getDuration() != null) {
            jsonObject.addProperty("duration", task.getDuration().toString());
        }

        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            jsonObject.addProperty("epicId", subtask.getEpicId());
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            jsonObject.add("subtasksIds", context.serialize(epic.getSubtasksIds()));
            if (epic.getEndTime() != null) {
                jsonObject.addProperty("endTime", epic.getEndTime().toString());
            }
        }

        return jsonObject;
    }

    @Override
    public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        String title = jsonObject.get("title").getAsString();
        String description = jsonObject.get("description").getAsString();
        Status status = Status.valueOf(jsonObject.get("status").getAsString());
        int idOfTask = jsonObject.get("idOfTask").getAsInt();

        LocalDateTime startTime = jsonObject.has("startTime") ? LocalDateTime.parse(jsonObject.get("startTime").getAsString()) : null;
        Duration duration = jsonObject.has("duration") ? Duration.parse(jsonObject.get("duration").getAsString()) : null;

        if (jsonObject.has("epicId")) {
            int epicId = jsonObject.get("epicId").getAsInt();
            return new Subtask(title, description, status, startTime, duration, epicId, idOfTask);
        } else if (jsonObject.has("subtasksIds")) {
            ArrayList<Integer> subtasksIds = context.deserialize(jsonObject.get("subtasksIds"), new TypeToken<ArrayList<Integer>>() {
            }.getType());
            LocalDateTime endTime = jsonObject.has("endTime") ? LocalDateTime.parse(jsonObject.get("endTime").getAsString()) : null;
            return new Epic(title, description, status, startTime, duration, subtasksIds, idOfTask);
        }

        return new Task(title, description, status, startTime, duration, idOfTask);
    }
}
