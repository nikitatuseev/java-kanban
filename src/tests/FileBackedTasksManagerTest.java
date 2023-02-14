package tests;

import exceptions.ManagerSaveException;
import managers.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends AbstractTaskManagerTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    public void setUp() {
        file = new File("resources/record2");
        taskManager = new FileBackedTasksManager(file);
        createTests();
    }

    @AfterEach
    public void clearFile() {
        file.delete();
    }

    @Test
    public void loadFromFileShouldThrowException() {
        ManagerSaveException ex = assertThrows(ManagerSaveException.class, () -> FileBackedTasksManager.loadFromFile(new File("test.csv")));
        assertEquals("Ошибка чтения", ex.getMessage());
    }

    //можно ли как-то упросить этот тест? я сделал проверку на все и получилось слишком много кода
    // и еще когда делаю проверку на отсортированный список после выгрузки он получается пустым, но так ведь
    //не должно быть и я не понимаю, где ошибка
    @Test
    public void loadFromFile() {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubTask(subtask1);
        taskManager.saveSubTask(subtask2);
        List<Task> sortList = new ArrayList<>();
        sortList.add(task);
        sortList.add(subtask1);
        sortList.add(subtask2);
        List<Task> expectedList = new ArrayList<>();
        expectedList.add(task);
        expectedList.add(subtask1);
        expectedList.add(subtask2);
        List<Task> actualList = new ArrayList<>(taskManager.getSortedTasks());
        assertEquals(expectedList, actualList);
        taskManager.getTaskById(1);
        taskManager.getSubTaskById(3);
        List<Task> listOfHistory = new ArrayList<>();
        listOfHistory.add(task);
        listOfHistory.add(subtask1);
        List<Task> allTask = new ArrayList<>();
        allTask.add(task);
        List<Task> allEpic = new ArrayList<>();
        allEpic.add(epic);
        List<Task> allSubTask = new ArrayList<>();
        allSubTask.add(subtask1);
        allSubTask.add(subtask2);
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(allTask, fileBackedTasksManager.getAllTask());
        assertEquals(allEpic, fileBackedTasksManager.getAllEpic());
        assertEquals(allSubTask, fileBackedTasksManager.getAllSubTask());
        assertEquals(listOfHistory, fileBackedTasksManager.getHistory());
        assertNotEquals(sortList, fileBackedTasksManager.getSortedTasks());
    }
}





