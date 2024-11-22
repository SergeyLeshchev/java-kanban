package manager;

import data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedListForHistory linkedListForHistory = new LinkedListForHistory();

    @Override
    public void addTaskInHistory(Task task) {
        if (task == null) {
            return;
        }
        linkedListForHistory.removeNode(linkedListForHistory.mapForHistory.get(task.getIdOfTask()));
        Node newNode = linkedListForHistory.linkLast(task);
        linkedListForHistory.mapForHistory.put(task.getIdOfTask(), newNode);
    }

    @Override
    public void remove(int id) {
        linkedListForHistory.removeNode(linkedListForHistory.mapForHistory.get(id));
    }

    @Override
    public ArrayList<Task> getHistory() {
        return linkedListForHistory.getTasks();
    }

    public class LinkedListForHistory {
        private final Map<Integer, Node> mapForHistory = new HashMap<>();

        private Node first;
        private Node last;

        private Node linkLast(Task task) {
            Node newNode = new Node(task, last, null);
            if (first == null) {
                first = newNode;
            } else {
                last.next = newNode;
            }
            last = newNode;
            return newNode;
        }

        private void removeNode(Node node) {
            if (node == null) {
                return;
            }
            if (node == first && node == last) {
                first = null;
                last = null;
            } else if (node.next == null) { // Если node - хвост
                node.prev.next = null;
                last = node.prev;
            } else if (node.prev == null) { // Если node - голова
                node.next.prev = null;
                first = node.next;
            } else {
                node.next.prev = node.prev;
                node.prev.next = node.next;
            }
            mapForHistory.remove(node.task.getIdOfTask());
        }

        private ArrayList<Task> getTasks() {
            ArrayList<Task> history = new ArrayList<>();
            Node node = linkedListForHistory.first;
            while (node != null) {
                history.add(node.task);
                node = node.next;
            }
            return history;
        }
    }

    public static class Node {
        private final Task task;
        private Node next;
        private Node prev;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }
}
