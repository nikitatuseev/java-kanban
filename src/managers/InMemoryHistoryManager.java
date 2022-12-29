package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> mapHistory = new HashMap<>();
    public CustomLinkedList<Task> list = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        Node<Task> node = new Node<>(null, task, null);
        if (mapHistory.containsKey(node.data.getId())) {
            Node forRemove = list.findNode(node.data);
            list.removeNode(forRemove);
            mapHistory.remove(node.data.getId());
        }
        list.linkLast(node.data);
        mapHistory.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        Node forRemove = list.findNode(mapHistory.get(id).data);
        list.removeNode(forRemove);
        mapHistory.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return list.getTasks();
    }

    static class CustomLinkedList<T extends Task> {
        private Node head;
        private Node tail;
        private int size = 0;

        public void linkLast(Task task) {
            final Node oldTail = tail;
            final Node newNode = new Node(tail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            size++;
        }

        List<Task> getTasks() {
            List<Task> tasksHistoryList = new ArrayList<>();
            Node node = head;
            while (node != null) {
                tasksHistoryList.add(node.data);
                node = node.next;
            }
            return tasksHistoryList;
        }

        public Node findNode(Task data) {
            Node current = head;
            while (current != null) {
                if (current.data == data) {
                    return current;
                }
                current = current.next;
            }
            return null;
        }

        public void removeNode(Node node) {
            if (node.prev != null) {
                node.prev.next = node.next;
                node.data = null;
            } else {
                head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
                node.data = null;
            }
        }

    }
}
