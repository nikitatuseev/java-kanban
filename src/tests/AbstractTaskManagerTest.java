package tests;

import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractTaskManagerTest<T extends TaskManager> {
    T taskManager;

    //я не понимаю почему в тестах на subTask если не сохранять обычный task то появляется ошибка и тест работает
    //только при сохранении обычного task
    Task task;

    Epic epic;

    Subtask subtask1;

    Subtask subtask2;

    @BeforeEach
    public void createTests() {


        task = new Task(1, StatusTask.NEW, "task1", "description", LocalDateTime.of(1, 1, 1, 1, 0), 30);

        epic = new Epic(2, StatusTask.NEW, "epic1", "описание");

        subtask1 = new Subtask(3, StatusTask.NEW, "sub1", "description", epic.getId(), LocalDateTime.of(1, 1, 1, 1, 30), 30);

        subtask2 = new Subtask(4, StatusTask.NEW, "sub2", "hh", epic.getId(), LocalDateTime.of(1, 1, 1, 2, 0), 30);

    }

    @Test
    void saveNullTask() {
        assertEquals(0, taskManager.getAllTask().size());
    }


    @Test
    void saveTask() {
        taskManager.saveTask(task);
        assertEquals(1, taskManager.getAllTask().size(), "количество задач не совпадает");
    }

    @Test
    void saveNullEpic() {
        assertEquals(0, taskManager.getAllEpic().size());
    }

    @Test
    void saveEpic() {
        taskManager.saveEpic(epic);
        assertEquals(1, taskManager.getAllEpic().size(), "количество эпиков не совпадает");
    }

    @Test
    void saveNullSubTask() {
        assertEquals(0, taskManager.getAllSubTask().size());
    }

    @Test
    void saveSubTask() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        assertEquals(2, taskManager.getAllSubTask().size(), "количество subtask не совпадает");
    }

    @Test
    void ListTaskShouldBeNull() {
        assertEquals(0, taskManager.getAllTask().size(), "размер списка не совпадает");
    }

    @Test
    void getAllTask() {
        taskManager.saveTask(task);
        List<Task> list = new ArrayList<>();
        list.add(task);
        assertEquals(list.size(), taskManager.getAllTask().size());
    }

    @Test
    void ListEpicShouldBeNull() {
        assertEquals(0, taskManager.getAllEpic().size(), "размер списка не совпадает");
    }

    @Test
    void getAllEpic() {
        taskManager.saveEpic(epic);
        List<Epic> list = new ArrayList<>();
        list.add(epic);
        assertEquals(list.size(), taskManager.getAllEpic().size());
    }

    @Test
    void ListSubTaskShouldBeNull() {
        assertEquals(0, taskManager.getAllSubTask().size(), "размер списка не совпадает");
    }

    @Test
    void getAllSubTask() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask2);
        taskManager.saveSubTask(subtask1);
        List<Subtask> list = new ArrayList<>();
        list.add(subtask1);
        list.add(subtask2);
        assertEquals(list.size(), taskManager.getAllSubTask().size());

    }

    @Test
    void getTaskById() {
        taskManager.saveTask(task);
        int id = task.getId();
        assertEquals(task, taskManager.getTaskById(id));
    }

    @Test
    void getTaskByWrongId() {//id всегда начинается с 1 так что 0 id не может быть
        taskManager.saveTask(task);
        assertNull(taskManager.getTaskById(0));
    }

    @Test
    void getEmptyTask() {
        Random rnd = new Random();
        assertNull(taskManager.getTaskById(rnd.nextInt(100)));
    }

    @Test
    void getEpicById() {
        taskManager.saveEpic(epic);
        int id = epic.getId();
        assertEquals(epic, taskManager.getEpicById(id));
    }

    @Test
    void getEpicByWrongId() {
        taskManager.saveEpic(epic);
        assertNull(taskManager.getEpicById(0));
    }

    @Test
    void getEmptyEpic() {
        Random rnd = new Random();
        assertNull(taskManager.getEpicById(rnd.nextInt(100)));
    }

    @Test
    void getSubTaskById() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        int id = subtask1.getId();
        assertEquals(subtask1, taskManager.getSubTaskById(id));
    }

    @Test
    void getSubTaskByWrongId() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        assertNull(taskManager.getSubTaskById(0));
    }

    @Test
    void getEmptySubTask() {
        Random rnd = new Random();
        assertNull(taskManager.getSubTaskById(rnd.nextInt(100)));
    }

    @Test
    void removeAllTask() {
        taskManager.saveTask(task);
        taskManager.removeAllTask();
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void removeEmptyAllTask() {
        taskManager.removeAllTask();
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void removeAllEpic() {
        taskManager.saveEpic(epic);
        taskManager.removeAllEpic();
        assertEquals(0, taskManager.getAllEpic().size());
    }

    @Test
    void removeEmptyAllEpic() {
        taskManager.removeAllEpic();
        assertEquals(0, taskManager.getAllEpic().size());
    }

    @Test
    void removeAllSubTask() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        taskManager.removeAllSubTask();
        assertEquals(0, taskManager.getAllSubTask().size());
    }

    @Test
    void removeEmptyAllSubTask() {
        taskManager.removeAllSubTask();
        assertEquals(0, taskManager.getAllSubTask().size());
    }

    @Test
    void removeTaskById() {
        taskManager.saveTask(task);
        int id = task.getId();
        taskManager.removeTaskById(id);
        assertNull(taskManager.getTaskById(id));
    }

    @Test
    void removeTaskByWrongId() {
        taskManager.saveTask(task);
        taskManager.removeTaskById(0);
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void removeEpicById() {
        taskManager.saveEpic(epic);
        int id = epic.getId();
        taskManager.removeEpicById(id);
        assertNull(taskManager.getEpicById(id));
    }

    @Test
    void removeEpicByWrongId() {
        taskManager.saveEpic(epic);
        taskManager.removeEpicById(0);
        assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void removeSubTaskById() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.removeSubTaskById(subtask1.getId());
        assertNull(taskManager.getSubTaskById(subtask1.getId()));
    }

    @Test
    void removeSubTaskByWrongId() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.removeSubTaskById(0);
        assertEquals(subtask1, taskManager.getSubTaskById(subtask1.getId()));
    }

    //так как при обновлении может поменяться не только статус, но и название или описание я сравниваю их полностью
    @Test
    void updateTaskOnDONE() {
        taskManager.saveTask(task);
        Task newTaskFirst = new Task(task.getId(), StatusTask.DONE, "task1", "description", LocalDateTime.of(1, 1, 1, 1, 0), 30);
        taskManager.updateTask(newTaskFirst);
        assertEquals(newTaskFirst, taskManager.getTaskById(task.getId()));
    }

    @Test
    void updateTaskOnIN_PROGRESS() {
        taskManager.saveTask(task);
        Task newTaskFirst = new Task("task1", "description", LocalDateTime.of(1, 1, 1, 1, 0), 30);
        newTaskFirst.setId(task.getId());
        newTaskFirst.setStatus(StatusTask.IN_PROGRESS);
        taskManager.updateTask(newTaskFirst);
        assertEquals(newTaskFirst, taskManager.getTaskById(task.getId()));
    }

    @Test
    void updateTask() {
        taskManager.saveTask(task);
        taskManager.updateTask(task);
        assertEquals(StatusTask.NEW, taskManager.getTaskById(task.getId()).getStatus());
    }

    @Test
    void updateEpic() {
        taskManager.saveEpic(epic);
        Epic newEpic = new Epic("epic1", "newEpic");
        newEpic.setId(epic.getId());
        taskManager.updateEpic(newEpic);
        assertEquals(newEpic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void updateSubTaskOnDONE() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        Subtask newSubTask = new Subtask(subtask1.getId(), StatusTask.DONE, "sub1", "description", epic.getId(), LocalDateTime.of(1, 1, 1, 1, 30), 30);
        taskManager.updateSubTask(newSubTask);
        assertEquals(newSubTask, taskManager.getSubTaskById(subtask1.getId()));
    }

    @Test
    void updateSubTaskOnIN_PROGRESS() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        Subtask newSubTask = new Subtask(subtask1.getId(), StatusTask.IN_PROGRESS, "sub1", "description", epic.getId(), LocalDateTime.of(1, 1, 1, 1, 30), 30);
        taskManager.updateSubTask(newSubTask);
        assertEquals(newSubTask, taskManager.getSubTaskById(subtask1.getId()));
    }

    @Test
    void CheckEndTimeTask() {
        taskManager.saveTask(task);
        task.setStartTime(LocalDateTime.of(1, 1, 1, 1, 0));
        task.setDuration(30);
        assertEquals(LocalDateTime.of(1, 1, 1, 1, 30), task.getEndTime());
    }

    @Test
    void CheckEndTimeSubTask() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        subtask1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 0));
        subtask1.setDuration(30);
        assertEquals(LocalDateTime.of(1, 1, 1, 1, 30), subtask1.getEndTime());
    }

    @Test
    void CheckDurationOfEpic() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        subtask1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 0));
        subtask1.setDuration(30);
        subtask2.setStartTime(LocalDateTime.of(1, 1, 1, 1, 30));
        subtask2.setDuration(60);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        assertEquals(90, epic.getDuration());
    }

    @Test
    void CheckEndTimeEpic() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        assertEquals(subtask2.getEndTime(), epic.getEndTime());
    }

    @Test
    void CheckStartTimeEpic() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        subtask1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 0));
        subtask1.setDuration(30);
        subtask2.setStartTime(LocalDateTime.of(1, 1, 1, 1, 30));
        subtask2.setDuration(60);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        assertEquals(subtask1.getStartTime(), epic.getStartTime());
    }

    @Test
    void CheckTimeEpicAfterRemoveFirstSubTask() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        subtask1.setStartTime(LocalDateTime.of(1, 1, 1, 1, 0));
        subtask1.setDuration(30);
        subtask2.setStartTime(LocalDateTime.of(1, 1, 1, 1, 30));
        subtask2.setDuration(60);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        taskManager.removeSubTaskById(subtask1.getId());
        assertEquals(subtask2.getStartTime(), epic.getStartTime());
    }
}

