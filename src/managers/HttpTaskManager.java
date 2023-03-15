package managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.KVTaskClient;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;

    private final Gson gson;

    public HttpTaskManager(String url) throws IOException, InterruptedException {
        super(null);
        client = new KVTaskClient(url);
        gson = Managers.getGson();
    }

    @Override
    public void save() {
        try {
            client.put("tasks", gson.toJson(getAllTask()));
            client.put("epic", gson.toJson(getAllEpic()));
            client.put("subtask", gson.toJson(getAllSubTask()));
            client.put("history", gson.toJson(historyManager.getHistory().stream()
                    .map(Task::getId)
                    .collect(Collectors.toList())));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static HttpTaskManager loadFromServer(String url) throws IOException, InterruptedException {
        HttpTaskManager managerFromServer = new HttpTaskManager(url);
        List<Task> historyManager = managerFromServer.getHistory();
        int maxId = 0;
        String jsonString = managerFromServer.client.load("tasks");
        if (!jsonString.isEmpty()) {
            Type taskListType = new TypeToken<List<Task>>() {
            }.getType();
            List<Task> tasks = managerFromServer.gson.fromJson(jsonString, taskListType);
            for (Task task : tasks) {
                managerFromServer.allTask.put(task.getId(), task);
                managerFromServer.addTaskForSorting(task);
                if (task.getId() > maxId) {
                    maxId = task.getId();
                }
            }
        }
        jsonString = managerFromServer.client.load("epic");
        if (!jsonString.isEmpty()) {
            Type epicListType = new TypeToken<List<Epic>>() {
            }.getType();
            List<Epic> epics = managerFromServer.gson.fromJson(jsonString, epicListType);
            for (Epic epic : epics) {
                managerFromServer.allEpic.put(epic.getId(), epic);
                if (epic.getId() > maxId) {
                    maxId = epic.getId();
                }
            }
        }
        jsonString = managerFromServer.client.load("subtask");
        if (!jsonString.isEmpty()) {
            Type subtasksListType = new TypeToken<List<Subtask>>() {
            }.getType();
            List<Subtask> subtasks = managerFromServer.gson.fromJson(jsonString, subtasksListType);
            for (Subtask subtask : subtasks) {
                managerFromServer.allSubTask.put(subtask.getId(), subtask);
                managerFromServer.addTaskForSorting(subtask);
                if (subtask.getId() > maxId) {
                    maxId = subtask.getId();
                }
            }
        }
        jsonString = managerFromServer.client.load("history");
        if (!jsonString.isEmpty()) {
            Type tasksListType = new TypeToken<List<Integer>>() {
            }.getType();
            List<Integer> history = managerFromServer.gson.fromJson(jsonString, tasksListType);
            for (Integer id : history) {
                if (managerFromServer.allEpic.containsKey(id)) {
                    historyManager.add(managerFromServer.allEpic.get(id));
                } else if (managerFromServer.allSubTask.containsKey(id)) {
                    historyManager.add(managerFromServer.allSubTask.get(id));
                } else if (managerFromServer.allTask.containsKey(id)) {
                    historyManager.add(managerFromServer.allTask.get(id));
                }
            }
        }
        return managerFromServer;
    }
}


