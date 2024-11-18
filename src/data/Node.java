package data;

import java.util.Objects;

public class Node<T> {
    private T task;
    private Node<T> next;
    private Node<T> prev;

    public Node(T task) {
        this.task = task;
    }

    public T getTask() {
        return this.task;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public Node<T> getPrev() {
        return this.prev;
    }

    public void setPrev(Node<T> node) {
        prev = node;
    }

    public void setNext(Node<T> node) {
        next = node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(task, node.task) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, next, prev);
    }
}
