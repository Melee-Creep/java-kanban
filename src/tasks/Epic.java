package tasks;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public void clearSubtasks() {
        subtaskList.clear();
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "id=" + getId() +
                ", name='" +getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
