package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private final String apiUrl;
    private final String apiToken;
    private final HttpClient httpClient;

    public KVTaskClient(String apiUrl) throws IOException, InterruptedException {
        this.apiUrl = apiUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.apiToken = register();
    }

    private String register() throws IOException, InterruptedException {
        String urlString = apiUrl + "/register";
        URI uri = URI.create(urlString);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new RuntimeException("Не удалось зарегистрироваться на сервере, код ответа: " + statusCode);
        }

        return response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        String urlString = apiUrl + "/save/" + key + "?API_TOKEN=" + apiToken;
        URI uri = URI.create(urlString);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new RuntimeException("Не удалось поместить данные на сервер, код ответа: " + statusCode);
        }
    }

    public String load(String key) throws IOException, InterruptedException {
        String urlString = apiUrl + "/load/" + key + "?API_TOKEN=" + apiToken;
        URI uri = URI.create(urlString);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode == 200) {
            return response.body();
        } else if (statusCode == 404) {
            throw new RuntimeException("Не найдены данные для ключа: " + key);
        } else {
            throw new RuntimeException("Не удалось загрузить данные с сервера, код ответа: " + statusCode);
        }
    }
}