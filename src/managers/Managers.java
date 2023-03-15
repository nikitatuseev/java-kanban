package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Managers {
    public static TaskManager getDefault() throws IOException, InterruptedException {
        String url = "http://localhost:8078";
        return new HttpTaskManager(url);
    }

    public static TaskManager getDefForTest() {
        return new InMemoryTaskManager();
    }

    public static TaskManager saveInMemory() {
        return new FileBackedTasksManager(new File("resources/record"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }
}
