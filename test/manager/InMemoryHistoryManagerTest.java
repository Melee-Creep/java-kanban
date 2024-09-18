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

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void getHistory10Tasks() {
        for (int i = 0; i < 16; i++) {
            taskManager.addTask(new Task(("задача " + i) , ("описание " + i)));
        }

        List<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            taskManager.getTask(task.getId());
        }

        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size(), "Неверное количество элементов в истории ");
    }

    @Test
    public void getHistory10Subtasks() {
        Epic epic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        for (int i = 0; i < 16; i++) {
            taskManager.getEpicById(0).addSubtask(new Subtask(("Подзадача " + i), ("Описание " + i), epic.getId()));
        }

        List<Subtask> tasks = taskManager.getSubtasks();
        for (Task task : tasks) {
            taskManager.getTask(task.getId());
        }

        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size(), "Неверное количество элементов в истории ");
    }
}