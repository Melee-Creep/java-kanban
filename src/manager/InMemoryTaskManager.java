package manager;

import exception.ManagerNotFoundException;
import exception.ManagerValidationException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Status;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int id = 0;

    final Map<Integer, Task> tasks = new HashMap<>();
    final Map<Integer, Subtask> subtasks = new HashMap<>();
    final Map<Integer, Epic> epics = new HashMap<>();
    final HistoryManager historyManager = Managers.getDefaultHistory();
    TaskPlanner planner = new TaskPlanner();
    private Map<LocalDateTime, Boolean> interval = planner.intervalTimeMap();

    @Override
    public int generateId() {
        return id++;
    }

    protected void setIdCounter(int id) {
        this.id = id;
    }

    @Override
    public Task addTask(Task task) {
        task.setId(generateId());
        taskTimeValidator(task);
        tasks.put(task.getId(), task);
        priorityTask.add(task);
        return task;
    }

    protected Task addTasks(Integer id, Task task) {
        if (task != null) {
            tasks.put(id, task);
            priorityTask.add(task);
            return task;
        }
        throw new ManagerNotFoundException("Задача пуста");
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(generateId());
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        taskTimeValidator(subtask);
        subtasks.put(subtask.getId(), subtask);
        priorityTask.add(subtask);
        updateEpicStatus(epic.getId());
        updateEpicTime(subtask.getEpicId());
        return subtask;
    }

    protected Subtask addSubtasks(Integer id, Subtask subtask) {
        if (subtask != null) {
            subtasks.put(id, subtask);
            return subtask;
        }
        throw new ManagerNotFoundException("Подзадача пустая");
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    protected Epic addEpics(Integer id, Epic epic) {
        if (epic != null) {
            epics.put(id, epic);
            return epic;
        }
        throw new ManagerNotFoundException("Нельзя создать пустой эпик");
    }

    private final Set<Task> priorityTask = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder()))
    );

    @Override
    public List<Task> getPriorityTasks() {
        return new ArrayList<>(priorityTask);
    }

    @Override
    public Map<Integer, Task> getAllTasks() {
        return tasks;
    }

    @Override
    public void removeAllTask() {
        priorityTask.clear();
        tasks.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return task;
        } else {
            throw new ManagerNotFoundException("Задачи с таким id нет");
        }
    }

    @Override
    public void removeTask(int id) {
        priorityTask.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    private int calculateEpicDuration(Integer id) {
        Epic epic = getEpicById(id);

        return epic.getSubtaskList().stream()
                .mapToInt(subtaskId -> subtasks.get(subtaskId).getDuration())
                .sum();
    }

    private LocalDateTime calculateEpicStartTime(Integer id) {
        Epic epic = getEpicById(id);
        return epic.getSubtaskList().stream()
                .map(subtasks::get)
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());
    }

    private LocalDateTime calculateEpicEndTime(Integer id) {
        Epic epic = getEpicById(id);
        return epic.getSubtaskList().stream()
                .map(subtasks::get)
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(epic.getStartTime());
    }

    public void updateEpicTime(Integer id) {
        Epic epic = getEpicById(id);
        epic.setDuration(calculateEpicDuration(id));
        epic.setStartTime(calculateEpicStartTime(id));
        epic.setEndTime(calculateEpicEndTime(id));
    }

    private void taskTimeValidator(Task task) {
        if (isCross(task)) {
            throw new ManagerValidationException("Время задачи пересекается с существующей");
        }
    }

    private boolean isCross(Task task) {
        LocalDateTime startTime = planner.updateInterval(task.getStartTime());
        LocalDateTime endTime = planner.updateInterval(task.getEndTime());
        while (startTime.isBefore(endTime)) {
            if (interval.get(startTime)) {
                return true;
            } else {
                updateInterval(startTime);
                startTime = startTime.plusMinutes(15);
            }
        }
        return startTime.equals(endTime) && interval.get(startTime);
    }

    private void updateInterval(LocalDateTime time) {
        interval.put(time, true);
    }

    private void resetTimeInterval(Task task) {
        LocalDateTime startTime = planner.updateInterval(task.getStartTime());
        LocalDateTime endTime = planner.updateInterval(task.getEndTime());
        while (startTime.isBefore(endTime)) {
            interval.put(startTime, false);
            startTime = startTime.plusMinutes(15);
        }
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (!tasks.containsKey(taskId)) {
            throw new ManagerNotFoundException("Задача отсутсвует");
        }
        Task oldTask = tasks.get(task.getId());
        resetTimeInterval(oldTask);
        taskTimeValidator(task);
        tasks.put(taskId, task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return epic;
        } else {
            throw new ManagerNotFoundException("Эпика с таким id не найдено");
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return subtasks.get(id);
        } else {
            throw new ManagerNotFoundException("Подзадачи с таким id не найдено");
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Integer> getEpicSubtasks(Epic epic) {
        return epic.getSubtaskList();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        priorityTask.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (!epics.containsKey(id)) {
            throw new ManagerNotFoundException("такого эпика нет");
        }
        List<Integer> epicSubtasks = epics.get(id).getSubtaskList();
        epics.remove(id);
        historyManager.remove(id);
        for (Integer subtask : epicSubtasks) {
            subtasks.remove(subtask);
            historyManager.remove(subtask);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtask(id);
        priorityTask.remove(subtasks.get(id));
        historyManager.remove(id);
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            oldEpic.setName(epic.getName());
            oldEpic.setDescription(epic.getDescription());
            return oldEpic;
        }
        throw new ManagerNotFoundException("Эпик не существует");
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask oldSubtask = subtasks.get(subtask.getId());
            priorityTask.remove(oldSubtask);
            resetTimeInterval(oldSubtask);
            taskTimeValidator(subtask);
            priorityTask.add(subtask);
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
            return subtask;
        } else {
            throw new ManagerNotFoundException("Подзадачи для обновления не существует");
        }

    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public String toString() {
        return "manager.TaskManager{" +
                "id=" + id +
                ", tasks=" + tasks +
                '}';
    }

    public static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    private void updateEpicStatus(Integer id) {
        Epic epic = epics.get(id);
        int doneSubtaskCount = 0;
        int newSubtaskCount = 0;
        List<Integer> list = epic.getSubtaskList();
        for (Integer tasks : list) {
            Subtask subtask = subtasks.get(tasks);
            if (subtask.getStatus() == Status.DONE) {
                doneSubtaskCount++;
            }
            if (subtask.getStatus() == Status.NEW) {
                newSubtaskCount++;
            }
        }
        if (doneSubtaskCount == list.size()) {
            epic.setStatus(Status.DONE);
        } else if (newSubtaskCount == list.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}