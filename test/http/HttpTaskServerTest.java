package http;

import com.google.gson.Gson;
import manager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = Managers.getGson();

    HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void init() {
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        manager.removeAllTask();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void shouldAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                Status.NEW);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing task 2");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> tasksFromManager = manager.getEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }


    @Test
    public void shouldGetTasks() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));
        Task task2 = manager.addTask(new Task("Вторая задача", "Помыть кружку", Status.NEW));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(manager.getTasks());
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks, "задачи не совпадают");
    }

    @Test
    public void shouldGetEpics() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Тестовая задача, заголовок", "Описание тестовой задачи"));
        Epic epic2 = manager.addEpic(new Epic("Тестовая задача 2, заголовок", "Описание тестовой задачи"));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(manager.getEpics());
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks, "задачи не совпадают");
    }

    @Test
    public void shouldGetSubtasks() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Тестовая задача, заголовок", "Описание тестовой задачи"));
        Subtask subtask = manager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));
        Subtask subtask2 = manager.addSubtask(new Subtask("Под.эпик2", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 13, 0), epic.getId()));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(manager.getSubtasks());
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks, "задачи не совпадают");
    }

    @Test
    public void shouldGetTask() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/?0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTask = gson.toJson(manager.getTask(task.getId()));
        String tasks = response.body();

        assertEquals(200, response.statusCode());
        assertEquals(expectedTask, tasks, "задачи не совпадают");
    }

    @Test
    public void shouldGetEpic() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Тестовая задача, заголовок", "Описание тестовой задачи"));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/?0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(manager.getEpicById(epic.getId()));
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks, "задачи не совпадают");
    }

    @Test
    public void shouldGetSubtask() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        Subtask subtask = manager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/?1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(manager.getSubtaskById(subtask.getId()));
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks, "задачи не совпадают");
    }

    @Test
    public void shouldDeleteTasks() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));
        Task task2 = manager.addTask(new Task("Вторая задача", "Помыть кружку", Status.NEW));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> expectedTasks = manager.getTasks();
        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size(), "задачи не удалены");
    }

    @Test
    public void shouldDeleteEpics() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Тестовая задача, заголовок", "Описание тестовой задачи"));
        Epic epic2 = manager.addEpic(new Epic("Тестовая задача 2, заголовок", "Описание тестовой задачи"));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> expectedTasks = manager.getEpics();
        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size(), "задачи не удалены");
    }

    @Test
    public void shouldDeleteSubtasks() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        Subtask subtask = manager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));
        Subtask subtask2 = manager.addSubtask(new Subtask("Под.эпик2", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 13, 0), epic.getId()));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> expectedTasks = manager.getSubtasks();
        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size(), "задачи не удалены");
    }

    @Test
    public void shouldDeleteTask() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Вторая задача", "Помыть кружку", Status.NEW));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/?0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> expectedTasks = manager.getTasks();

        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size(), "задачи не удалены");
    }

    @Test
    public void shouldDeleteEpic() throws IOException, InterruptedException {
        Epic epic = manager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/?0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> expectedTasks = manager.getEpics();

        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size(), "задачи не удалены");
    }


    @Test
    public void shouldGetHistory() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));
        Task task2 = manager.addTask(new Task("Вторая задача", "Помыть кружку", Status.NEW));
        manager.getTask(0);
        manager.getTask(1);

        List<Task> historyList = manager.getHistory();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(historyList);
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks, "задачи не совпадают");
    }

    @Test
    public void shouldGetPrioritized() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));
        Task task2 = manager.addTask(new Task("Вторая задача", "Помыть кружку", Status.NEW));

        List<Task> historyList = manager.getPriorityTasks();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(historyList);
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks, "задачи не совпадают");
    }

    @Test
    public void httpGetException404Test() throws IOException, InterruptedException {
        Task task = manager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/?100");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
        assertEquals("Задача с идентификатором 100 не найдена", response.body());

    }

    @Test
    public void httpPostException406Test() throws IOException, InterruptedException {

        Task task = manager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW, 100, LocalDateTime.now()));
        Task task2 = new Task("Вторая задача", "Помыть кружку", Status.NEW);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
        assertEquals("Задача не создана", response.body());
    }
}

