package tests;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;

import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EpicTest {
    private static TaskManager manager;

    @BeforeEach
    public void BeforeEach() {
        manager = Managers.getDefault();
        Epic epic = new Epic("epic1", "описание");
        manager.saveEpic(epic);
    }

    @Test
    public void ShouldBeNullSubTask() {
        Assertions.assertEquals(StatusTask.NEW, manager.getEpicById(1).getStatus());
    }

    @Test
    public void AllSubTaskStatusNEW() {
        Subtask firstSubTask = new Subtask("sub1", "description",
                manager.getEpicById(1).getId(), LocalDateTime.of(1, 1, 1, 1, 30), 30);
        Subtask secondSubTask = new Subtask("sub2", "hh",
                manager.getEpicById(1).getId(), LocalDateTime.of(1, 1, 1, 2, 0), 30);
        manager.saveSubTask(firstSubTask);
        manager.saveSubTask(secondSubTask);
        assertEquals(StatusTask.NEW, manager.getEpicById(1).getStatus());
    }

    @Test
    public void AllSubTaskStatusDONE() {
        Subtask firstSubTask = new Subtask(2, StatusTask.DONE, "sub1", "description",
                manager.getEpicById(1).getId(), LocalDateTime.of(1, 1, 1, 1, 30), 30);
        Subtask secondSubTask = new Subtask(3, StatusTask.DONE, "sub2", "hh",
                manager.getEpicById(1).getId(), LocalDateTime.of(1, 1, 1, 2, 0), 30);
        manager.saveSubTask(firstSubTask);
        manager.saveSubTask(secondSubTask);
        assertEquals(StatusTask.DONE, manager.getEpicById(1).getStatus());
    }

    @Test
    public void StatusSubTaskNEWAndDONE() {
        Subtask firstSubTask = new Subtask(2, StatusTask.NEW, "sub1", "description",
                manager.getEpicById(1).getId(), LocalDateTime.of(1, 1, 1, 1, 30), 30);
        Subtask secondSubTask = new Subtask(3, StatusTask.DONE, "sub2", "hh",
                manager.getEpicById(1).getId(), LocalDateTime.of(1, 1, 1, 2, 0), 30);
        manager.saveSubTask(firstSubTask);
        manager.saveSubTask(secondSubTask);
        assertEquals(StatusTask.IN_PROGRESS, manager.getEpicById(1).getStatus());
    }

    @Test
    public void AllSubTaskStatusIN_PROGRESS() {
        Subtask firstSubTask = new Subtask(2, StatusTask.IN_PROGRESS, "sub1", "description",
                manager.getEpicById(1).getId(), LocalDateTime.of(1, 1, 1, 1, 30), 30);
        Subtask secondSubTask = new Subtask(3, StatusTask.IN_PROGRESS, "sub2", "hh",
                manager.getEpicById(1).getId(), LocalDateTime.of(1, 1, 1, 2, 0), 30);
        manager.saveSubTask(firstSubTask);
        manager.saveSubTask(secondSubTask);
        assertEquals(StatusTask.IN_PROGRESS, manager.getEpicById(1).getStatus());
    }
}

