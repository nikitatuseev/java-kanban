package managers;

import tasks.Task;

public class Node<E> {
    public Task data;
    public Node<E> next;
    public Node<E> prev;

    public Node(Node<E> prev, Task data, Node<E> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }


}
