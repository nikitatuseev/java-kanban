package managers;

import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    LinkedList<Task> listOfViewedTask = new LinkedList<>();

    @Override
    public void add(Task task) {
        listOfViewedTask.add(task);
        if (listOfViewedTask.size() > 10) {
            listOfViewedTask.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return listOfViewedTask;
    }
}
