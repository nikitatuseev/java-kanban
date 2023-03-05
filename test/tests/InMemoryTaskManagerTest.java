package tests;

import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.StatusTask;
import tasks.Subtask;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends AbstractTaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        createTests();
    }

    @Test
    void getSubtasksByNameEpic() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        List<Integer> list = taskManager.getEpicById(epic.getId()).getListIdOfSubTask();
        List<Subtask> listSubTask = new ArrayList<>();
        for (Integer i : list) {
            listSubTask.add(taskManager.getSubTaskById(i));
        }
        assertEquals(listSubTask, taskManager.getSubtasksByNameEpic(epic.getId()));
    }

    @Test
    void updateEpicStatusDONE() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        subtask1.setStatus(StatusTask.DONE);
        subtask2.setStatus(StatusTask.DONE);
        taskManager.updateEpicStatus(subtask1);
        assertEquals(StatusTask.DONE, epic.getStatus());
    }

    @Test
    void updateEpicStatusIN_PROGRESS() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        subtask2.setStatus(StatusTask.DONE);
        taskManager.updateEpicStatus(subtask1);
        assertEquals(StatusTask.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void updateEpicStatus() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        subtask1.setStatus(StatusTask.IN_PROGRESS);
        subtask2.setStatus(StatusTask.DONE);
        taskManager.updateEpicStatus(subtask1);
        assertEquals(StatusTask.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void updateEpicStatusNEW() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        taskManager.updateEpicStatus(subtask1);
        assertEquals(StatusTask.NEW, epic.getStatus());
    }
}


