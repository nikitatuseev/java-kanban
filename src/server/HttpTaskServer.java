package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import managers.HttpTaskManager;
import managers.Managers;

import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer(HttpTaskManager taskManager) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task", this::handleTask);
        server.createContext("/tasks/epic", this::handleEpic);
        server.createContext("/tasks/subtask", this::handleSubtask);
        server.createContext("/tasks/history", this::handleHistory);
        server.createContext("/tasks", this::handlePriority);
        gson = Managers.getGson();
        this.taskManager = taskManager;
    }

    public HttpTaskServer() throws IOException, InterruptedException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task", this::handleTask);
        server.createContext("/tasks/epic", this::handleEpic);
        server.createContext("/tasks/subtask", this::handleSubtask);
        server.createContext("/tasks/history", this::handleHistory);
        server.createContext("/tasks", this::handlePriority);
        gson = Managers.getGson();
        this.taskManager = Managers.getDefault();
    }

    public void handlePriority(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String response;
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                response = gson.toJson(taskManager.getSortedTasks());
                writeResponse(httpExchange, response);
                return;
            } else {
                System.out.println("Ожидался запрос GET,а получился метод " + method);
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleHistory(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String response;
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET")) {
                response = gson.toJson(taskManager.getHistory());
                writeResponse(httpExchange, response);
                return;
            } else {
                System.out.println("Ожидался запрос GET,а получился метод " + method);
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleEpic(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getQuery();
            String method = httpExchange.getRequestMethod();
            String response;
            switch (method) {
                case "GET": {
                    if (query == null) {
                        response = gson.toJson(taskManager.getAllEpic());
                        writeResponse(httpExchange, response);
                        return;
                    } else if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (taskManager.getEpicById(id) != null) {
                            response = gson.toJson(taskManager.getEpicById(id));
                            writeResponse(httpExchange, response);
                            return;
                        } else {
                            response = gson.toJson("Получен некорректный id");
                            writeResponse(httpExchange, response);
                        }
                        break;
                    }
                }
                case "DELETE": {
                    if (query == null) {
                        taskManager.removeAllEpic();
                        response = gson.toJson("epic удалены");
                        writeResponse(httpExchange, response);
                        return;
                    } else if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (taskManager.getEpicById(id) != null) {
                            taskManager.removeEpicById(id);
                            response = gson.toJson("epic с id= " + id + " удалена");
                            writeResponse(httpExchange, response);
                        } else {
                            response = gson.toJson("Получен некорректный id");
                            writeResponse(httpExchange, response);
                        }
                        break;
                    }
                    break;
                }
                case "POST": {
                    handlePostEpic(httpExchange);
                    break;
                }
                default: {
                    System.out.println("Ожидался запрос GET, DELETE, или POST, а получился метод " + method);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleTask(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getQuery();
            String method = httpExchange.getRequestMethod();
            String response;
            switch (method) {
                case "GET": {
                    if (query == null) {
                        response = gson.toJson(taskManager.getAllTask());
                        writeResponse(httpExchange, response);
                        return;
                    } else if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (taskManager.getTaskById(id) != null) {
                            response = gson.toJson(taskManager.getTaskById(id));
                            writeResponse(httpExchange, response);
                            return;
                        } else {
                            response = gson.toJson("Получен некорректный id");
                            writeResponse(httpExchange, response);
                        }
                        break;
                    }
                }
                case "DELETE": {
                    if (query == null) {
                        taskManager.removeAllTask();
                        response = gson.toJson("задачи удалены");
                        writeResponse(httpExchange, response);
                        return;
                    } else if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (taskManager.getTaskById(id) != null) {
                            taskManager.removeTaskById(id);
                            response = gson.toJson("task с id= " + id + " удалена");
                            writeResponse(httpExchange, response);
                        } else {
                            response = gson.toJson("Получен некорректный id");
                            writeResponse(httpExchange, response);
                        }
                        break;
                    }
                    break;
                }
                case "POST": {
                    handlePostTask(httpExchange);
                    break;
                }
                default: {
                    System.out.println("Ожидался запрос GET, DELETE, или POST, а получился метод " + method);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (IOException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleSubtask(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getQuery();
            String method = httpExchange.getRequestMethod();
            String response;
            switch (method) {
                case "GET": {
                    if (query == null) {
                        response = gson.toJson(taskManager.getAllSubTask());
                        writeResponse(httpExchange, response);
                        return;
                    } else if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (Pattern.matches("^/tasks/subtask/epic/$", path)) {
                            if (taskManager.getSubtasksByNameEpic(id) != null) {
                                response = gson.toJson(taskManager.getSubtasksByNameEpic(id));
                                writeResponse(httpExchange, response);
                                return;
                            }
                        } else if (taskManager.getSubTaskById(id) != null) {
                            response = gson.toJson(taskManager.getSubTaskById(id));
                            writeResponse(httpExchange, response);
                            return;
                        } else {
                            response = gson.toJson("Получен некорректный id");
                            writeResponse(httpExchange, response);
                        }
                        break;
                    }
                }
                case "DELETE": {
                    if (query == null) {
                        taskManager.removeAllSubTask();
                        response = gson.toJson("subtask удалены");
                        writeResponse(httpExchange, response);
                        return;
                    } else if (Pattern.matches("^id=\\d+$", query)) {
                        String pathId = query.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (taskManager.getSubTaskById(id) != null) {
                            taskManager.removeSubTaskById(id);
                            response = gson.toJson("subtask с id= " + id + " удалена");
                            writeResponse(httpExchange, response);
                        } else {
                            response = gson.toJson("Получен некорректный id");
                            writeResponse(httpExchange, response);
                        }
                        break;
                    }
                    break;
                }
                case "POST": {
                    handlePostSubtask(httpExchange);
                    break;
                }
                default: {
                    System.out.println("Ожидался запрос GET, DELETE, или POST, а получился метод " + method);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handlePostTask(HttpExchange httpExchange) throws IOException, InstantiationException {
        String body = readText(httpExchange);
        Task task;
        try {
            task = gson.fromJson(body, Epic.class);
            if ((task.getName() == null) || (task.getDescription() == null)) {
                writeResponse(httpExchange, "поля не должны быть пустыми");
                return;
            }
        } catch (JsonSyntaxException e) {
            writeResponse(httpExchange, "получен некорректный JSON");
            return;
        }
        int id = task.getId();
        if (id == 0) {
            taskManager.saveTask(task);
            writeResponse(httpExchange, "task сохранен");
            return;
        } else if (taskManager.getEpicById(id) != null) {
            taskManager.updateTask(task);
            writeResponse(httpExchange, "task обновлен");
            return;
        }
        writeResponse(httpExchange, "task с id " + id + " не найден");
    }

    private void handlePostSubtask(HttpExchange httpExchange) throws IOException {
        String body = readText(httpExchange);
        Subtask subtask;
        try {
            subtask = gson.fromJson(body, Subtask.class);
            if ((subtask.getName() == null) || (subtask.getDescription() == null) || subtask.getIdOfEpic() == 0) {
                writeResponse(httpExchange, "Поля не могут быть пустыми");
                return;
            }
        } catch (JsonSyntaxException e) {
            writeResponse(httpExchange, "Получен некорректный JSON");
            return;
        }
        int id = subtask.getId();
        int epicId = subtask.getIdOfEpic();
        if (taskManager.getEpicById(epicId) == null) {
            writeResponse(httpExchange, "Эпик с id " + epicId + " не найден");
            return;
        }
        if (id == 0) {
            taskManager.saveSubTask(subtask);
            writeResponse(httpExchange, "subtask сохранен");
            return;
        } else if (taskManager.getSubTaskById(id) != null) {
            taskManager.updateSubTask(subtask);
            writeResponse(httpExchange, "subtask обновлен");
            return;
        }
        writeResponse(httpExchange, "subtask с id " + id + " не найден");
    }

    private void handlePostEpic(HttpExchange httpExchange) throws IOException {
        String body = readText(httpExchange);
        Epic epic;
        try {
            epic = gson.fromJson(body, Epic.class);
            if ((epic.getName() == null) || (epic.getDescription() == null)) {
                writeResponse(httpExchange, "поля не должны быть пустыми");
                return;
            }
        } catch (JsonSyntaxException e) {
            writeResponse(httpExchange, "получен некорректный JSON");
            return;
        }
        int id = epic.getId();
        if (id == 0) {
            taskManager.saveEpic(epic);
            writeResponse(httpExchange, "Эпик сохранен");
            return;
        } else if (taskManager.getEpicById(id) != null) {
            taskManager.updateEpic(epic);
            writeResponse(httpExchange, "Эпик обновлен");
            return;
        }
        writeResponse(httpExchange, "Эпик с id " + id + " не найден");

    }

    private void writeResponse(HttpExchange httpExchange, String responseString) throws IOException {
        if (responseString.isBlank()) {
            httpExchange.sendResponseHeaders(httpExchange.getResponseCode(), 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            httpExchange.sendResponseHeaders(httpExchange.getResponseCode(), bytes.length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        httpExchange.close();
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks");
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
