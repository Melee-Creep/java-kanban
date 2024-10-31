package manager;

import converter.StringConverter;
import exception.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTasksManager(HistoryManager historyManager) {
        super();
        this.file = new File("File.csv");
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackUp = new FileBackedTasksManager(file);
        fileBackUp.readFile();
        return fileBackUp;
    }

    private void readFile() {
        int maxId = 0;
        int id;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isBlank()) {
                    break;
                }
                Task task = StringConverter.taskFromString(line);
                if (task != null) {
                    id = task.getId();
                    if (maxId < id) {
                        maxId = id;
                    }
                    if (task.getType() == Type.TASK) {
                        super.addTasks(id, task);
                    } else if (task.getType() == Type.SUBTASK) {
                        super.addSubtasks(id, (Subtask) task);
                    } else if (task.getType() == Type.EPIC) {
                        super.addEpics(id, (Epic) task);
                    }
                    taskManager.setIdCounter(maxId);
                }
            }
            if (!subtasks.isEmpty()) {
                for (Subtask s : subtasks.values()) {
                    Epic epic = epics.get(s.getEpicId());
                    epic.addSubtask(s.getId());
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла." + file.getAbsolutePath(), e);
        }
    }

    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write("id,type,title,status,description,epicId\n");
            for (Task task : getTasks()) {
                bufferedWriter.write(StringConverter.taskToString(task) + "\n");
            }
            for (Subtask subTask : getSubtasks()) {
                bufferedWriter.write(StringConverter.taskToString(subTask) + "\n");
            }
            for (Epic epic : getEpics()) {
                bufferedWriter.write(StringConverter.taskToString(epic) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла." + file.getAbsolutePath(), e);
        }
    }

    @Override
    public Task addTask(Task task) {
        Task newTask =  super.addTask(task);
        save();
        return newTask;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Subtask newSubtask = super.addSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic newEpic = super.addEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Map<Integer, Task> getAllTasks() {  // a.
        return super.getAllTasks();
    }

    @Override
    public void removeAllTask() { // b.
        super.removeAllTask();
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        return task;
    }

    @Override
    public void removeTask(int id) { // f.
        super.removeTask(id);
        save();
    }

    @Override
    public Task updateTask(Task task) {
        Task newTask = super.updateTask(task);
        save();
        return newTask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        return subtask;
    }

    @Override
    public List<Task> getTasks() {
        return super.getTasks();
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public List<Integer> getEpicSubtasks(Epic epic) {
        return super.getEpicSubtasks(epic);
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic newEpic = super.updateEpic(epic);
        save();
        return newEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask newSubtask = super.updateSubtask(subtask);
        save();
        return newSubtask;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

}
