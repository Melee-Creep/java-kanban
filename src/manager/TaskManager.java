package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {
    int generateId();

    Task addTask(Task task);  //

    Subtask addSubtask(Subtask subtask); //

    Epic addEpic(Epic epic); //

    Map<Integer, Task> getAllTasks();

    void removeAllTask(); //

    Task getTask(int id); //

    void removeTask(int id); //

    Task updateTask(Task task); //

    Epic getEpicById(int id); //

    Subtask getSubtaskById(int id); //

    List<Task> getTasks(); //

    List<Epic> getEpics();//

    List<Subtask> getSubtasks();//

    List<Integer> getEpicSubtasks(Epic epic);//

    void deleteAllEpics(); //

    void deleteAllSubtasks();//

    void deleteEpicById(int id); //

    void deleteSubtaskById(int id); //

    Epic updateEpic(Epic epic); //

    Subtask updateSubtask(Subtask subtask); //

    List<Task> getHistory();

}
