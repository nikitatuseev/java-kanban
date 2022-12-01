package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;


import java.util.List;

public interface TaskManager {
    void saveTask(Task task);

    void saveEpic(Epic epic);

    void saveSubTask(Subtask subtask);

    List<Task> getAllTask();

    List<Epic> getAllEpic();

    List<Subtask> getAllSubTask();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubTaskById(int id);

    List<Subtask> getAllSubByNameEpic(int id);

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubTask();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    void updateTask(Task task);

    void updateSubTask(Subtask subtask);

    void updateEpicStatus(Subtask subtask);

    void updateEpic(Epic epic);

    List<Task> getHistory();
}

