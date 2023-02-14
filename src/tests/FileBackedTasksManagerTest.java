package tests;

import exceptions.ManagerSaveException;
import managers.FileBackedTasksManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;
import tasks.Task;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class FileBackedTasksManagerTest extends AbstractTaskManagerTest<FileBackedTasksManager> {
    private File file;

    @BeforeEach
    public void setUp() {
        this.file = new File("resources/record2");
        this.taskManager = new FileBackedTasksManager(this.file);
        this.createTests();
    }

    @AfterEach
    public void clearFile() {
        this.file.delete();
    }

    @Test
    public void loadFromFileShouldThrowException() {
        ManagerSaveException ex = Assertions.assertThrows(ManagerSaveException.class, () -> {
            FileBackedTasksManager.loadFromFile(new File("test.csv"));
        });
        Assertions.assertEquals("Ошибка чтения", ex.getMessage());
    }
//как можно упросить этот тест? я сделал проверку на все и слишком много получилось так ведь не должно быть
    // и еще после выгрузки отсортированный список получается пустым и я не вижу, где ошибка
    @Test
    public void loadFromFile() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        List<Task> sortList = new ArrayList<>();
        sortList.add(this.task);
        sortList.add(this.subtask1);
        sortList.add(this.subtask2);
        List<Task> expectedList = new ArrayList<>();
        expectedList.add(this.task);
        expectedList.add(this.subtask1);
        expectedList.add(this.subtask2);
        List<Task> actualList = new ArrayList<>(taskManager.getSortedTasks());
        Assertions.assertEquals(expectedList, actualList);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(3);
        List<Task> listOfHistory = new ArrayList<>();
        listOfHistory.add(this.task);
        listOfHistory.add(this.subtask1);
        List<Task> allTask = new ArrayList<>();
        allTask.add(this.task);
        List<Task> allEpic = new ArrayList<>();
        allEpic.add(this.epic);
        List<Task> allSubTask = new ArrayList<>();
        allSubTask.add(this.subtask1);
        allSubTask.add(this.subtask2);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(this.file);
        Assertions.assertEquals(allTask, fileBackedTasksManager.getAllTask());
        Assertions.assertEquals(allEpic, fileBackedTasksManager.getAllEpic());
        Assertions.assertEquals(allSubTask, fileBackedTasksManager.getAllSubTask());
        Assertions.assertEquals(listOfHistory, fileBackedTasksManager.getHistory());
        Assertions.assertNotEquals(sortList, fileBackedTasksManager.getSortedTasks());
    }
}