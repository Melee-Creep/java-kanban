package converter;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class StringConverter {

    public static Task taskFromString(String line) {
        String[] splitTask;
        splitTask = line.split(",");
        int id = Integer.parseInt(splitTask[0]);
        Type type = Type.valueOf(splitTask[1]);
        String name = splitTask[2];
        Status status = Status.valueOf(splitTask[3]);
        String description = splitTask[4];

        switch (type) {
            case SUBTASK : {
                int epicId = Integer.parseInt(splitTask[5]);
                Subtask subTask = new Subtask(name, description, epicId);
                subTask.setId(id);
                return subTask;
            }
            case TASK : {
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            }
            case EPIC : {
                List<Integer> epicSubtasks = new ArrayList<>();
                for (int i = 5; i < splitTask.length; i++) {
                    epicSubtasks.add(Integer.parseInt(splitTask[i]));
                }
                Epic epic = new Epic(name, description, status);
                epic.setId(id);
                epic.setStatus(status);
                epic.setSubtaskList(epicSubtasks);
                return epic;
            }
            default : System.out.println("Ошибка при формировании задач из строки");
        }
        return null;
    }

    public static String taskToString(Task task) {
        String type = String.valueOf(task.getType());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(task.getId()).append(",");
        stringBuilder.append(type).append(",");
        stringBuilder.append(task.getName()).append(",");
        stringBuilder.append(task.getStatus().toString()).append(",");
        stringBuilder.append(task.getDescription());
        switch (type) {
            case "EPIC", "TASK":
                return stringBuilder.toString();
            case "SUBTASK":
                stringBuilder.append(",");
                stringBuilder.append(((Subtask) task).getEpicId());
                return stringBuilder.toString();
            default:
                return null;
        }
    }
}