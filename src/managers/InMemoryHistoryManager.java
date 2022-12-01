package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> listOfViewedTask = new ArrayList<>();

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
