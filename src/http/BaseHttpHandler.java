package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;
import tasks.Type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class BaseHttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final TaskManager taskManager;
    protected final Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        System.out.println("Обрабатывается запрос " + exchange.getRequestURI().toString() + " с методом " + exchange.getRequestMethod());
        byte[] response = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void writeResponse(HttpExchange exchange, String text, int responseCode) throws IOException {
        byte[] response = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, response.length);
            os.write(response);
        }
    }

    protected void handleDeleteObject(HttpExchange exchange, Type type) throws IOException {
        Optional<Integer> id = getId(exchange);
        if (id.isEmpty()) {
            writeResponse(exchange, "Некорректный формат id", 400);
            return;
        }
        boolean success = false;
        String response = "";

        switch (type) {
            case TASK:
                if (taskManager.getTask(id.get()) != null) {
                    taskManager.removeTask(id.get());
                    response = "Задача " + id.get() + " успешно удалена";
                    success = true;
                }
                break;
            case EPIC:
                if (taskManager.getEpicById(id.get()) != null) {
                    taskManager.deleteEpicById(id.get());
                    response = "Эпик и его подзадачи" + id.get() + " успешно удалены";
                    success = true;
                }
                break;
            case SUBTASK:
                if (taskManager.getSubtaskById(id.get()) != null) {
                    taskManager.deleteSubtaskById(id.get());
                    response = "Задача " + id.get() + " успешно удалена";
                    success = true;
                }
                break;
        }
        if (success) {
            sendText(exchange, response);
            return;
        }
        writeResponse(exchange, "Задача с идентификатором " + id.get() + " не найдена", 404);
    }

    protected <T> void handlePostObject(HttpExchange exchange, Class<T> c, Consumer<T> creator) throws IOException {
        InputStream bodyInputStream = exchange.getRequestBody();
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (body.isEmpty()) {
            writeResponse(exchange, "Тело задачи не может быть пустым", 400);
            return;
        }

        try {
            T object = gson.fromJson(body, c);
            creator.accept(object);
            String response = "Задача создана";
            writeResponse(exchange, response, 201);
        } catch (Exception e) {
            writeResponse(exchange, "Задача не создана", 406);
        }
    }

    protected <T> void handlePostObjectUpdate(HttpExchange exchange, Class<T> c, Consumer<T> update) throws IOException {
        InputStream bodyInputStream = exchange.getRequestBody();
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (body.isEmpty()) {
            writeResponse(exchange, "Тело задачи не может быть пустым", 400);
            return;
        }

        try {
            T object = gson.fromJson(body, c);
            update.accept(object);
            String response = "Задача обновлена";
            writeResponse(exchange, response, 201);
        } catch (Exception e ) {
            writeResponse(exchange, "Задача не обновлена", 406);
        }
    }

    protected void handleGetObject(HttpExchange exchange, Function<Integer, ? extends Task> getter) throws IOException {

        Optional<Integer> id = getId(exchange);

        if (id.isEmpty()) {
            writeResponse(exchange, "Некорректный формат id", 400);
            return;
        }

        try {
         Task objectById = getter.apply(id.get());
         String response = gson.toJson(objectById);
         sendText(exchange, response);
        } catch (Exception e) {
            writeResponse(exchange, "Задача с идентификатором " + id.get() + " не найдена", 404);
        }
    }

    protected Optional<Integer> getId(HttpExchange exchange) {

        try {
            return Optional.of(Integer.parseInt(exchange.getRequestURI().getQuery()));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    protected Endpoint getEndpoint(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();

        if (requestMethod.equals("GET") && query == null) {
            return Endpoint.GET_ALL;
        }
        if (requestMethod.equals("DELETE") && query == null) {
            return Endpoint.DELETE_ALL;
        }
        if (requestMethod.equals("GET") && query != null) {
            return Endpoint.GET;
        }
        if (requestMethod.equals("POST")) {
            return Endpoint.POST;
        }
        if (requestMethod.equals("DELETE") && query != null) {
            return Endpoint.DELETE;
        }
        return Endpoint.UNKNOWN;
    }
}

