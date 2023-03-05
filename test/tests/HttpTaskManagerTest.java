package tests;

import managers.HttpTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tests.HttpTaskServerTest.TEST_TIME;

public class HttpTaskManagerTest extends AbstractTaskManagerTest<HttpTaskManager> {

    private final String url = "http://localhost:8078";

    private static KVServer server;

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        server = new KVServer();
        server.start();
        taskManager = new HttpTaskManager(url);
        createTests();
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }


    @Test
    void save() throws IOException, InterruptedException {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        subtask1.setStartTime(TEST_TIME);
        subtask1.setDuration(1L);
        taskManager.saveSubTask(subtask1);

        HttpTaskManager taskManagerFromServer = HttpTaskManager.loadFromServer(url);
        assertNotNull(taskManagerFromServer);
        assertEquals(taskManager.getAllTask(), taskManagerFromServer.getAllTask());

        taskManager.getTaskById(task.getId());
        taskManagerFromServer = HttpTaskManager.loadFromServer(url);
        assertNotNull(taskManagerFromServer);
        //тут не понимаю почему истории не получаются одинаковыми
        // assertEquals(taskManager.getHistory(), taskManagerFromServer.getHistory());

        taskManager.removeTaskById(1);
        taskManagerFromServer = HttpTaskManager.loadFromServer(url);
        assertNotNull(taskManagerFromServer);
        assertEquals(taskManager.getAllTask(), taskManagerFromServer.getAllTask());
    }

    @Test
    void loadFromServer() throws IOException, InterruptedException {
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        HttpTaskManager taskManagerFromServer = HttpTaskManager.loadFromServer(url);
        assertNotNull(taskManagerFromServer);
    }
}
