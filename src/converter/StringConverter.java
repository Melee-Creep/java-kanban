package converter;

import tasks.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StringConverter {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static Task taskFromString(String line) {
        String[] splitTask;
        splitTask = line.split(",");
        int id = Integer.parseInt(splitTask[0]);
        Type type = Type.valueOf(splitTask[1]);
        String name = splitTask[2];
        Status status = Status.valueOf(splitTask[3]);
        String description = splitTask[4];
        int duration = Integer.parseInt(splitTask[5]);
        LocalDateTime startTime = LocalDateTime.parse(splitTask[6], formatter);

        switch (type) {
            case SUBTASK : {
                int epicId = Integer.parseInt(splitTask[7]);
                Subtask subTask = new Subtask(name, description, status, duration, startTime, epicId);
                subTask.setId(id);
                return subTask;
            }
            case TASK : {
                Task task = new Task(name, description, status, duration, startTime);
                task.setId(id);
                return task;
            }
            case EPIC : {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                epic.setDuration(duration);
                epic.setStartTime(startTime);
                return epic;
            }
            default : System.out.println("Ошибка при формировании задач из строки");
        }
        return null;
    }

    public static String taskToString(Task task) {
        String type = String.valueOf(task.getType());

        return  switch (type) {
            case "EPIC", "TASK"-> String.join(",", Integer.toString(task.getId()), type, task.getName(),
                    task.getStatus().toString(), task.getDescription(), Integer.toString(task.getDuration()),
                    task.getStartTime().format(formatter), null);
            case  "SUBTASK" -> String.join(",", Integer.toString(task.getId()), type, task.getName(),
                    task.getStatus().toString(), task.getDescription(), Integer.toString(task.getDuration()),
                    task.getStartTime().format(formatter), Integer.toString(((Subtask)task).getEpicId()));
            default -> null;
        };
    }
}