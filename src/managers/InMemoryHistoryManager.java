package managers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList listOfHistory = new CustomLinkedList();

    @Override
    public void add(Task task) {
        Node node = new Node(null, task, null);
        if (task != null) {
            int id = node.data.getId();
            if (listOfHistory.nodeMap.containsKey(id)) {
                remove(id);
            }
            listOfHistory.linkLast(node.data);
        }
    }

    @Override
    public void remove(int id) {
        Node forRemove = listOfHistory.findNode(id);
        listOfHistory.removeNode(forRemove);
    }

    @Override
    public List<Task> getHistory() {
        return listOfHistory.getTasks();
    }

    static class CustomLinkedList {
        Map<Integer, Node> nodeMap;
        private Node head;
        private Node tail;
        private int size = 0;

        public CustomLinkedList() {
            nodeMap = new HashMap<>();
        }

        public void linkLast(Task task) {
            final Node oldTail = tail;
            final Node newNode = new Node(tail, task, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            nodeMap.put(task.getId(), newNode);
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

        private Node findNode(int id) {
            return nodeMap.get(id);
        }

        public void removeNode(Node node) {
            if (node.prev != null) {
                if (node.next == null) {
                    tail = node.prev;
                    tail.next = null;
                }
                if (node.next != null) {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                }
            } else {
                if (node.next == null) {
                    head = null;
                    tail = null;
                }
                if (node.next != null) {
                    head = node.next;
                    head.prev = null;
                }
            }
            nodeMap.remove(node.data.getId());
        }
    }
}
