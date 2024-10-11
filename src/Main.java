import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = taskManager.addTask(new Task("Первая задача","Попить чаю", Status.NEW));
        Task task1 = taskManager.addTask(new Task("Вторая задача","Покушать булку", Status.NEW));
        System.out.println("Список всех задач" + taskManager.getAllTasks());

        System.out.println("Ищем задачу по айди первая задача = " + taskManager.getTask(0));
        //  taskManager.removeTask(0);
        //  System.out.println("Удалили первую задачу, все задачи= " + taskManager.getAllTasks());

        //   taskManager.removeAllTask();
        //   System.out.println("Удалили все задачи= " + taskManager.getAllTasks());

        Epic epic = taskManager.addEpic(new Epic("Эпик","Большой эпик",Status.NEW));
        System.out.println(epic);

        Subtask subtask = taskManager.addSubtask(new Subtask("Под.эпик", "эпик1",epic.getId()));
        Subtask subtask1 = taskManager.addSubtask(new Subtask("Под.эпик1", "эпик1",epic.getId()));
        System.out.println(epic.getSubtaskList());
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        System.out.println(epic.getSubtaskList());
        System.out.println(epic);
        System.out.println("\n");
        System.out.println("*****");
        System.out.println("История");
        System.out.println(taskManager.getTask(0));
        System.out.println(taskManager.getEpicById(2));
        System.out.println(taskManager.getSubtaskById(3));
        System.out.println("\n");
        System.out.println("*****");
        System.out.println("Удалили задачу - должна уйти история");
        taskManager.removeTask(0);
        taskManager.deleteSubtaskById(3);
        InMemoryTaskManager.printAllTasks(taskManager);
    }
}