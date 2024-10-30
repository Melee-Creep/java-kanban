package manager;

import tasks.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task item;
        Node next;
        Node prev;

        public Node(Node prev, Task item, Node next) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private final HashMap<Integer, Node> historyTask;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        this.historyTask = new HashMap<>();
    }

    public void linkLast(Task element) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, element, null);
        tail = newNode;
        historyTask.put(element.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (!(currentNode == null)) {
            tasks.add(currentNode.item);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    public void removeNode(Node node) {
        if (!(node == null)) {
            final Node next = node.next;
            final Node prev = node.prev;
            node.item = null;

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node) { // ??
                head = next;
                head.prev = null;
            } else if (tail == node) {  // ??
                tail = prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        if (historyTask.get(id) != null) {
            removeNode(historyTask.get(id));
        }
    }
}