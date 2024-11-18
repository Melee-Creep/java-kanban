package manager;

import exception.ManagerNotFoundException;
import exception.ManagerSaveException;
import exception.ManagerValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Managers должен")
public abstract class ManagersTest<T extends TaskManager> {
    protected T taskManager;
    Task task;
    Epic epic;
    Subtask subtask;

    abstract T createManager();

    @BeforeEach
    void beforeEach() {
        taskManager = createManager();
    }

    @Test
    void createTaskGetAndPutToMap() {
        task = taskManager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));
        List<Task> tasks = taskManager.getTasks();

        assertEquals(task, tasks.getFirst(), "задачи не совпадают");
        assertEquals(1, tasks.size(), "в маше другое количество задач или она пустая");
    }

    @Test
    void createEpicGetAndPutToMap() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        List<Epic> epics = taskManager.getEpics();

        assertEquals(epic, epics.getFirst(), "эпики не совпадают");
        assertEquals(1, epics.size(), "в мапе другое количество задач или она пустая");
    }

    @Test
    void createSubtaskGetAndPutToMap() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));
        List<Subtask> subtasks = taskManager.getSubtasks();

        assertEquals(subtask, subtasks.getFirst(), "подзадачи на совпадают");
        assertEquals(1, subtasks.size(), "в маше другое количество задач или она пустая");
    }

    @Test
    void AddCreatedSubtaskToEpic() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));
        List<Integer> epicSubtasks = epic.getSubtaskList();
        Integer id = subtask.getId();

        assertEquals(id, epicSubtasks.getFirst(), "id подзадачи не совпадает");
        assertEquals(1, epicSubtasks.size(), "в эпике другое количество подзадач");
    }

    @Test
    void getTasks() {
        task = taskManager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));
        Task task1 = taskManager.addTask(new Task("Вторая задача", "Помыть кружку", Status.NEW));

        List<Task> tasks = taskManager.getTasks();

        Task taskResult = tasks.getFirst();
        Task taskResult1 = tasks.getLast();
        assertEquals(task, taskResult, "задачи не совпадают");
        assertEquals(task1, taskResult1, "Вторые задачи не совпадают");
        assertEquals(2, tasks.size(), "в мапе не две задачи");
    }

    @Test
    void getEpics() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        Epic epic1 = taskManager.addEpic(new Epic("Второй эпик", "Второй большой эпик", Status.NEW));

        List<Epic> epics = taskManager.getEpics();

        Epic resultEpic = epics.getFirst();
        Epic resultEpic1 = epics.getLast();
        assertEquals(epic, resultEpic, "Эпики не совпадают");
        assertEquals(epic1, resultEpic1, "Вторые эпики не совпадают");
        assertEquals(2, epics.size(), "в мапе не два эпика");
    }

    @Test
    void getSubTasks() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));
        Subtask subtask1 = taskManager.addSubtask(new Subtask("Под.эпик2", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 13, 0), epic.getId()));

        List<Subtask> subtasks = taskManager.getSubtasks();

        Subtask resultSubtask = subtasks.getFirst();
        Subtask resultSubtask1 = subtasks.getLast();
        assertEquals(subtask, resultSubtask, "Подзадачи не совпадают");
        assertEquals(subtask1, resultSubtask1, "Вторые подзадачи не совпадают");
        assertEquals(2, subtasks.size(), "в мапе не две подзадачи");
    }

    @Test
    void getEpicSubtasks() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));
        Subtask subtask1 = taskManager.addSubtask(new Subtask("Под.эпик2", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 13, 0), epic.getId()));

        List<Integer> subtasks = epic.getSubtaskList();

        Integer subtaskResultId = subtasks.getFirst();
        Integer subtaskResultId1 = subtasks.getLast();

        assertEquals(subtask.getId(), subtaskResultId, "id позадач не совпадают");
        assertEquals(subtask1.getId(), subtaskResultId1, "id подзадач 2 не совпадают");
        assertEquals(2, subtasks.size(), "у эпика не 2 подзадачи");
    }

    @Test
    void getTaskById() {
        task = taskManager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));

        Task taskResult = taskManager.getTask(task.getId());

        assertEquals(task, taskResult, "задачи не совпадают");
    }

    @Test
    void getSubTaskById() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));

        Subtask subTaskResult = taskManager.getSubtaskById(subtask.getId());

        assertEquals(subtask, subTaskResult, "подзадачи не совпадают");
    }

    @Test
    void getEpicById() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));

        Epic epicResult = taskManager.getEpicById(epic.getId());

        assertEquals(epic, epicResult, "эпики не совпадают");
    }

    @Test
    void updateTaskGetAndPutToMap() {
        task = taskManager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));
        Task savedTask = taskManager.addTask(task);
        savedTask.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(savedTask);

        List<Task> tasks = taskManager.getTasks();
        assertEquals(savedTask, tasks.getFirst(), "Задачи не совпадают");
    }

    @Test
    void updateSubTaskGetAndPutToMapAndChangeEpicStatus() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));
        subtask.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(subtask);

        assertEquals(epic.getStatus(), Status.IN_PROGRESS, "статус эпика не обновился");
    }

    @Test
    void updateSubTask() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));
        subtask.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(subtask);

        assertEquals(Status.IN_PROGRESS, subtask.getStatus(), "статус подзадачи не обновился");

    }

    @Test
    void updateEpicStatusToProgressAccordingCreateSubtask() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));

        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.IN_PROGRESS,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "статус эпика не изменился");
    }

    @Test
    void updateEpicStatusToDoneAndNewAccordingCreateSubtask() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));

        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.DONE,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));

        assertEquals(Status.DONE, epic.getStatus(), "статус эпика не изменился");
    }

    @Test
    void setNewEpicStatusForEpicWithoutSubtasks() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.DONE,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));

        taskManager.deleteAllSubtasks();

        assertEquals(epic.getStatus(), Status.NEW, "Статусы не совпадают");
    }

    @Test
    void updateEpicGetAndPutToMap() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        epic.setName("Обновленное описание эпика");
        epic.setDescription("Обновленный эпик");

        taskManager.updateEpic(epic);

        assertEquals("Обновленный эпик", epic.getDescription(), "Заголовок эпика не обновился");
        assertEquals("Обновленное описание эпика", epic.getName(), "Описание эпика не обновилось");
    }

    @Test
    void deleteTasksFromMap() {
        task = taskManager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));
        Task task1 = taskManager.addTask(new Task("Вторая задача", "Помыть кружку", Status.NEW));
        Integer size = taskManager.getTasks().size();

        taskManager.removeAllTask();

        Integer sizeResult = taskManager.getTasks().size();
        assertEquals(2, size, "мапа задач пуста");
        assertEquals(0, sizeResult, "мапа задач не пуста");
    }

    @Test
    void deleteEpicsFromMap() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        Epic epic1 = taskManager.addEpic(new Epic("Второй эпик", "Второй большой эпик", Status.NEW));
        Integer size = taskManager.getEpics().size();

        taskManager.deleteAllEpics();

        Integer sizeResult = taskManager.getEpics().size();
        assertEquals(2, size, "мапа задач пуста");
        assertEquals(0, sizeResult, "мапа задач не пуста");
    }

    @Test
    void deleteTaskFromMapById() {
        task = taskManager.addTask(new Task("Первая задача", "Попить чаю", Status.NEW));
        Task task1 = taskManager.addTask(new Task("Вторая задача", "Помыть кружку", Status.NEW));

        taskManager.removeTask(task.getId());

        List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "в мапе не одна задача");
        assertEquals(task1, tasks.getFirst(), "задачи не совпадают");
    }

    @Test
    void deleteEpicFromMapByID() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        Epic epic1 = taskManager.addEpic(new Epic("Второй эпик", "Второй большой эпик", Status.NEW));

        taskManager.deleteEpicById(epic.getId());

        List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "в мапе не один эпик");
        assertEquals(epic1, epics.getFirst(), "эпики не совпадают");
    }

    @Test
    void managerNotFoundExceptionTest() {
        ManagerNotFoundException thrown = assertThrows(ManagerNotFoundException.class,
                () -> taskManager.updateTask(new Task(null, null)));
        assertTrue(thrown.getMessage().contains("Задача отсутсвует"));
    }

    @Test
    void managerSaveExceptionTest() {
        File File = new File("a file that does not exist");
        ManagerSaveException thrown = assertThrows(ManagerSaveException.class,
                () -> FileBackedTasksManager.loadFromFile(File));
        assertTrue(thrown.getMessage().contains("Произошла ошибка во время чтения файла."));
    }

    @Test
    void managerValidationExceptionTest() {
        epic = taskManager.addEpic(new Epic("Эпик", "Большой эпик", Status.NEW));
        subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", Status.NEW,
                10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId()));

        ManagerValidationException thrown = assertThrows(ManagerValidationException.class,
                () -> taskManager.addSubtask(new Subtask("Под.эпик2", "эпик1", Status.NEW,
                        10, LocalDateTime.of(2024, 1, 1, 12, 0), epic.getId())));
        assertTrue(thrown.getMessage().contains("Время задачи пересекается с существующей"));
    }
}

