package tests;

import exceptions.ManagerSaveException;
import managers.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends AbstractTaskManagerTest<FileBackedTasksManager> {
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = new File("resources/record2");
        Files.createFile(file.toPath());
        taskManager = new FileBackedTasksManager(file);
        createTests();
    }

    @AfterEach
    public void clearFile() {
        assertTrue(file.delete());
    }

    @Test
    public void loadFromFileShouldThrowException() {
        ManagerSaveException ex = Assertions.assertThrows(ManagerSaveException.class, () -> {
            FileBackedTasksManager.loadFromFile(new File("test.csv"));
        });
        Assertions.assertEquals("Ошибка чтения", ex.getMessage());
    }

    @Test
    public void loadFromFile() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task);
        expectedList.add(subtask1);
        expectedList.add(subtask2);
        assertEquals(expectedList, taskManager.getSortedTasks());
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(3);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(taskManager.getAllTask(), fileBackedTasksManager.getAllTask());
        assertEquals(taskManager.getAllEpic(), fileBackedTasksManager.getAllEpic());
        assertEquals(taskManager.getAllSubTask(), fileBackedTasksManager.getAllSubTask());
        assertEquals(taskManager.getHistory(), fileBackedTasksManager.getHistory());
        assertEquals(taskManager.getSortedTasks(), fileBackedTasksManager.getSortedTasks());
    }
}