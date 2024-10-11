package manager;

import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    private static TaskManager taskManager;
    private static HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void emptyHistory() {
        assertEquals(0, historyManager.getHistory().size(), "История не пустая");
    }

    @Test
    public void addHistory() {
        Task task = taskManager.addTask(new Task("Первая задача","Попить чаю", Status.NEW));
        Task task1 = taskManager.addTask(new Task("Вторая задача","Покушать булку", Status.NEW));
        Epic epic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        Subtask subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1",epic.getId()));
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(subtask);
        assertEquals(4, historyManager.getHistory().size(), "История не добавлена");
    }

    @Test
    public void deleteTwoHistory() {
        Task task = taskManager.addTask(new Task("Первая задача","Попить чаю", Status.NEW));
        Task task1 = taskManager.addTask(new Task("Вторая задача","Покушать булку", Status.NEW));
        Epic epic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        Subtask subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1",epic.getId()));
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.remove(0);
        historyManager.remove(1);
        assertEquals(2, historyManager.getHistory().size(), "Удалилось неверное количество историй");
    }
    @Test
    public void duplicateHistory() {
        Task task = taskManager.addTask(new Task("Первая задача","Попить чаю", Status.NEW));
        Task task1 = taskManager.addTask(new Task("Вторая задача","Покушать булку", Status.NEW));
        Epic epic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        Subtask subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1",epic.getId()));
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(epic);
        historyManager.add(subtask);
        historyManager.add(task);
        historyManager.add(task1);
        assertEquals(4, historyManager.getHistory().size(), "Имеются дубликаты в истории");
    }
}