package manager;

import data.Node;
import data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> mapForHistory = new HashMap<>();
    private final LinkedListForHistory<Task> linkedListForHistory = new LinkedListForHistory<>();

    @Override
    public void addTaskInHistory(Task task) {
        if (task == null) {
            return;
        }
        if (mapForHistory.containsKey(task.getIdOfTask())) {
            linkedListForHistory.removeNode(mapForHistory.get(task.getIdOfTask()));
        }
        Node<Task> newNode = linkedListForHistory.linkLast(task);
        mapForHistory.put(task.getIdOfTask(), newNode);
    }

    @Override
    public void remove(int id) {
        linkedListForHistory.removeNode(mapForHistory.get(id));
        mapForHistory.remove(id);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> history = new ArrayList<>();
        Node<Task> node = linkedListForHistory.first;
        while (history.size() != linkedListForHistory.size) {
            history.add(node.getTask());
            node = node.getNext();
        }
        return history;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    public class LinkedListForHistory<T> {
        private int size = 0;
        private Node<T> first;
        private Node<T> last;

        private Node<T> linkLast(T task) {
            Node<T> newNode = new Node<>(task);
            if (size > 0) {
                newNode.setPrev(last);
                last.setNext(newNode);
            } else {
                first = newNode;
            }
            last = newNode;
            size++;
            return newNode;
        }

        private void removeNode(Node<T> node) {
            if (node == null) {
                return;
            }
            if (size == 1) {
                first = null;
                last = null;
            } else if (node.getNext() == null) { // Если node - хвост
                node.getPrev().setNext(null);
                last = node.getPrev();
            } else if (node.getPrev() == null) { // Если node - голова
                node.getNext().setPrev(null);
                first = node.getNext();
            } else {
                node.getNext().setPrev(node.getPrev());
                node.getPrev().setNext(node.getNext());
            }
            size--;
        }
    }
}
