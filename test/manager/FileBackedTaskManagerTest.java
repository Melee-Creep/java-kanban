package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.util.Collections;

public class FileBackedTaskManagerTest {

    private static File file;
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();


    @BeforeEach
    public void beforeEach() {
        file = new File("testFile.csv");
        taskManager = new FileBackedTasksManager(file);
    }

    @Test
    public void shouldSaveEmptyListOfTask() {
        Assertions.assertEquals(Collections.emptyList(), taskManager.getTasks(), "Лист не сохранён");
    }

    @Test
    public void shouldLoadEmptyListOfTask() {
        taskManager.deleteAllEpics();
        taskManager.removeAllTask();
        FileBackedTasksManager loadEmptyFileManager = FileBackedTasksManager.loadFromFile(file);
        Assertions.assertEquals(Collections.emptyList(), loadEmptyFileManager.getTasks(), "Файл не пустой");
    }

    @Test
    public void shouldLoadListOfTask() {
        Task newTask = taskManager.addTask(new Task("Новая первая задача","Попить чаю", Status.NEW));
        Epic newEpic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        Subtask newSubtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1", newEpic.getId()));
        Subtask newSubtask1 = taskManager.addSubtask(new Subtask("Под.эпик1", "эпик1", newEpic.getId()));

        FileBackedTasksManager loadFileManager = FileBackedTasksManager.loadFromFile(file);

        Assertions.assertEquals(loadFileManager.getTasks().get(newTask.getId()), newTask,
                "Задачи не загруженны или задачи не совпадают");
        Assertions.assertEquals(loadFileManager.getEpicById(newEpic.getId()), newEpic,
                "Задачи не загруженны или задачи не совпадают");
        Assertions.assertEquals(loadFileManager.getSubtaskById(newSubtask.getId()), newSubtask,
                "Задачи не загруженны или задачи не совпадают");
        Assertions.assertEquals(loadFileManager.getSubtaskById(newSubtask1.getId()), newSubtask1,
                "Задачи не загруженны или задачи не совпадают");
    }
}