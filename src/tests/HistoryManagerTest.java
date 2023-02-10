package tests;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest extends InMemoryHistoryManager {
    HistoryManager historyManager = Managers.getDefaultHistory();
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
    void addTask() {
        historyManager.add(task);
        assertEquals(task, historyManager.getHistory().get(0));
    }

    @Test
    void addEpic() {
        historyManager.add(epic);
        Task[] list = {epic};
        assertArrayEquals(list, historyManager.getHistory().toArray());
    }

    @Test
    void addSubTask() {
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        Task[] list = {epic, subtask1, subtask2};
        assertArrayEquals(list, historyManager.getHistory().toArray());
    }

    @Test
    void addAll() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(subtask2);
        Task[] list = {task, epic, subtask1, subtask2};
        assertArrayEquals(list, historyManager.getHistory().toArray());
    }

    @Test
    void addNull() {
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void addWithDuplication() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(task);
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    void remove() {
        historyManager.add(task);
        historyManager.remove(task.getId());
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void removeFromStart() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.remove(task.getId());
        Task[] list = {epic, subtask1};
        assertArrayEquals(list, historyManager.getHistory().toArray());
    }

    @Test
    void removeFromMiddle() {
        historyManager.add(epic);
        historyManager.add(subtask1);
        historyManager.add(task);
        historyManager.remove(subtask1.getId());
        Task[] list = {epic, task};
        assertArrayEquals(list, historyManager.getHistory().toArray());
    }

    @Test
    void removeFromEnd() {
        historyManager.add(task);
        historyManager.add(epic);
        historyManager.add(subtask1);
        Task[] list = {task, epic};
        historyManager.remove(subtask1.getId());
        assertArrayEquals(list, historyManager.getHistory().toArray());
    }

    @Test
    void removeWithEmptyHistory() {
        historyManager.remove(1);
        assertEquals(0, historyManager.getHistory().size());
    }

    @Test
    void getEmptyHistory() {
        assertEquals(0, historyManager.getHistory().size());
    }
}

