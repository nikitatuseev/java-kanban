package managers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    public CustomLinkedList list = new CustomLinkedList();

    @Override
    public void add(Task task) {
        Node node = new Node(null, task, null);
        if (task != null) {
            if (list.nodeMap.containsKey(node.data.getId())) {
                Node forRemove = list.findNode(node.data.getId());
                list.removeNode(forRemove);
            }
            list.linkLast(node.data);
        }
    }

    @Override
    public void remove(int id) {
        Node forRemove = list.findNode(id);
        list.removeNode(forRemove);
    }

    @Override
    public List<Task> getHistory() {
        return list.getTasks();
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

        public Node findNode(int id) {
            return nodeMap.get(id);
        }

        public void removeNode(Node node) {
            //если я использую первый вариант то возникает ошибка и я не понимаю как ее исправить. Второй вариант
            //как я проверил работает при всех случаях но мне все равно кажется что он неправильный

           /*if (nodeMap.size()== 1) {
                head = null;
                tail = null;
            } else {
                if (node.prev != null) {
                    if (node.next == null) {
                    tail = node.prev;
                    tail.next = null;
                }
                    node.prev.next = node.next;
            } else {
                head = node.next;
                head.prev = null;
            }
            if (node.next != null) {
                if (node.prev == null) {
                    head = node.next;
                    head.prev = null;
                } else {
                    node.next.prev = node.prev;
                    node.data = null;
                }
            }
           }
           nodeMap.remove(node.data.getId());

            */
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            }
            nodeMap.remove(node.data.getId());
        }
    }
}
