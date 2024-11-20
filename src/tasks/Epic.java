package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtaskList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    public List<Integer> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Integer> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public void setEndTime(LocalDateTime endTime) {
    }

    public void clearSubtasks() {
        subtaskList.clear();
    }

    public void deleteSubtask(Integer id) {
        this.subtaskList.remove(id);
    }

    public void addSubtask(Integer id) {
        this.subtaskList.add(id);
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", type=" + getType() +
                ", subtaskList=" + subtaskList +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }
}
