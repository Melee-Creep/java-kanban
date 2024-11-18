import manager.FileBackedTasksManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        File file1 = new File("File.csv");
        FileBackedTasksManager taskManager1 = new FileBackedTasksManager(file1);
        Task newTask = taskManager1.addTask(new Task("Новая первая задача","Попить чаю", Status.NEW,200, LocalDateTime.now()));
        Epic newEpic = taskManager1.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        Subtask newSubtask = taskManager1.addSubtask(new Subtask("Под.эпик", "эпик1", Status.DONE,100, LocalDateTime.of(2024,12,1, 12,00), newEpic.getId()));
        Subtask newSubtask1 = taskManager1.addSubtask(new Subtask("Под.эпик2","эпик2", Status.DONE,10, LocalDateTime.of(2024,12,1, 18,00), newEpic.getId()));
        FileBackedTasksManager backedTasksManager = FileBackedTasksManager.loadFromFile(file1);
        System.out.println("\n****\n backup");
        System.out.println(backedTasksManager.getAllTasks());
        System.out.println(backedTasksManager.getSubtasks());
        System.out.println(backedTasksManager.getEpics());
    }
}