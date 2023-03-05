package tests;

import com.google.gson.Gson;
import managers.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.net.HttpURLConnection;


public class HttpTaskServerTest {
    protected static final LocalDateTime TEST_TIME=LocalDateTime.of(1,1,1,2,30);
    private static final int PORT = 8080;
    private static HttpTaskServer server;
    private final Gson gson= Managers.getGson();

    @BeforeEach
    public void setup() throws IOException {
        server=new HttpTaskServer();
        server.start();
    }

    @AfterEach
    public void teardown() {
        server.stop();
    }
//я начал делать тесты, но зашел в тупик и не могу понять, что делаю не так
    @Test
    public void testHandleTask() throws IOException, InterruptedException {
        Task task =new Task("task1","description");
        URI url=URI.create("http://localhost:8080/tasks/task/");
        HttpClient client=HttpClient.newHttpClient();
        String json=gson.toJson(task);
        HttpRequest.BodyPublisher body=HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request=HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());

        assertEquals(201,response.statusCode());
    }

    @Test
    public void testHandleEpic() throws IOException {

    }

    @Test
    public void testHandleSubtask() throws IOException {

    }

    @Test
    public void testHandleHistory() throws IOException {

    }

}

