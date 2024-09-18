package manager;

import org.junit.jupiter.api.*;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.List;


public class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void addNewTask() {
        Task task = taskManager.addTask(new Task("Первая задача","Попить чаю", Status.NEW));
        Task task1 = taskManager.getTask(task.getId());
        Assertions.assertNotNull(task, "Задача не создана");
        Assertions.assertEquals(task1, task, "Задачи не совпадают");

        List<Task> tasks = taskManager.getTasks();
        Assertions.assertNotNull(tasks, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void AddNewEpicAndSubtask() {
        Epic epic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        Subtask subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1",epic.getId()));
        Subtask subtask1 = taskManager.getSubtaskById(subtask.getId());
        Epic epic1 = taskManager.getEpicById(epic.getId());
        Assertions.assertNotNull(epic, "Эпик не создан");
        Assertions.assertNotNull(subtask, "Под.эпик не создан");
        Assertions.assertEquals(epic1, epic, "Эпики не совпадают");
        Assertions.assertEquals(subtask1, subtask, "Под.эпик не совпадает");

        List<Subtask> subtasks = taskManager.getSubtasks();
        List<Epic> epics = taskManager.getEpics();
        Assertions.assertNotNull(epics, "Эпики не возвращаются");
        Assertions.assertNotNull(subtasks, "Подзадачи не возвращаются");
        Assertions.assertEquals(1, epics.size(), "Неверное количество эпиков.");
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    public void DeleteTasks() {
        Task task = taskManager.addTask(new Task("Первая задача","Попить чаю", Status.NEW));
        Task task1 = taskManager.addTask(new Task("Вторая задача","Покушать булку", Status.NEW));
        taskManager.removeAllTask();
        List<Task> tasks = taskManager.getTasks();
        Assertions.assertTrue(tasks.isEmpty(), "Спикос задач не пустой");
    }

    @Test
    public void DeleteTaskWithId() {
        Task task = taskManager.addTask(new Task("Первая задача","Попить чаю", Status.NEW));
        Task task1 = taskManager.addTask(new Task("Вторая задача","Покушать булку", Status.NEW));
        taskManager.removeTask(0);
        List<Task> tasks = taskManager.getTasks();
        Assertions.assertEquals(1, tasks.size(), "Задача не удалена");
    }

    @Test
    public void DeleteEpicWithId() {
        Epic epic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        taskManager.deleteEpicById(0);
        List<Epic> epics = taskManager.getEpics();
        Assertions.assertEquals(0, epics.size(), "Эпик не удален");
    }

    @Test
    public void DeleteSubtaskWithId() {
        Epic epic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        Subtask subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", epic.getId()));
        Subtask subtask1 = taskManager.addSubtask(new Subtask("Под.эпик1", "эпик1",epic.getId()));
        taskManager.deleteSubtaskById(1);
        List<Subtask> subtasks = taskManager.getEpicById(epic.getId()).getSubtaskList();
        Assertions.assertEquals(1, subtasks.size(), "Подзадача не удалена");
    }

    @Test
    public void UpdateStatusEpicAndSubtask() {
        Epic epic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        Subtask subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", epic.getId()));
        Subtask subtask1 = taskManager.addSubtask(new Subtask("Под.эпик1", "эпик1",epic.getId()));
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус не обновляется");
        Assertions.assertEquals(Status.DONE, subtask.getStatus(), "Статус не обновляется");
    }

    @Test
    public void UpdateStatusTask() {
        Task task = taskManager.addTask(new Task("Первая задача","Попить чаю", Status.NEW));
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        Assertions.assertEquals(Status.IN_PROGRESS, task.getStatus(), "Статус не обновляется");
    }
}
