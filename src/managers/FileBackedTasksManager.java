package managers;


import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected Path path = Path.of("resources/record");

    public FileBackedTasksManager() {
        super();
    }

    public FileBackedTasksManager(Path path) {
        super();
        this.path = path;
    }

    @Override
    public void saveSubTask(Subtask subtask) {
        super.saveSubTask(subtask);
        save();
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public ArrayList<Task> getAllTask() {
        super.getAllTask();
        save();
        return super.getAllTask();
    }

    @Override
    public List<Epic> getAllEpic() {
        super.getAllEpic();
        save();
        return super.getAllEpic();
    }

    @Override
    public List<Subtask> getAllSubTask() {
        super.getAllSubTask();
        save();
        return super.getAllSubTask();
    }

    @Override
    public Task getTaskById(int id) {
        super.getTaskById(id);
        save();
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) {
        super.getEpicById(id);
        save();
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        super.getSubTaskById(id);
        save();
        return super.getSubTaskById(id);
    }

    @Override
    public List<Subtask> getSubtasksByNameEpic(int id) {
        super.getSubtasksByNameEpic(id);
        save();
        return super.getSubtasksByNameEpic(id);
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    protected void updateEpicStatus(Subtask subtask) {
        super.updateEpicStatus(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public List<Task> getHistory() {
        save();
        return super.getHistory();
    }

    private void save() {
        final String historyInString = historyToString((InMemoryHistoryManager) historyManager);
        try (FileWriter fileWriter = new FileWriter(pathToSaveData())) {
            for (Task task : allTask.values()) {
                fileWriter.write(taskToString(task));
                fileWriter.write("\n");
            }
            for (Epic epic : allEpic.values()) {
                fileWriter.write(taskToString(epic));
                fileWriter.write("\n");
            }
            for (Subtask subtask : allSubTask.values()) {
                fileWriter.write(taskToString(subtask));
                fileWriter.write("\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyInString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String taskToString(Task task) {
        if (task.getClass() == Subtask.class) {
            return SubTaskToString((Subtask) task);
        } else if (task.getClass() == Task.class) {
            return task.getId() + "," + "TASK" + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription();
        } else {
            return task.getId() + "," + "EPIC" + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription();
        }
    }

    private String SubTaskToString(Subtask task) {
        return task.getId() + "," + "SUBTASK" + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + task.getIdOfEpic();
    }

    private String historyToString(InMemoryHistoryManager historyManager) {
        ArrayList<Integer> taskId = new ArrayList<>(historyManager.listOfHistory.nodeMap.keySet());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < taskId.size(); i++) {
            if (i != taskId.size() - 1) {
                stringBuilder.append(taskId.get(i)).append(",");
            } else {
                stringBuilder.append(taskId.get(i));
            }
        }
        return stringBuilder.toString();
    }

    public static String pathToSaveData() {
        String content = null;
        content = "resources/record";
        return content;
    }

    public void loadFromFile(File file) throws FileNotFoundException, IllegalAccessException {
        List<String> lines = new ArrayList<>();
        FileReader reader = new FileReader("resources/record");
        try (BufferedReader br = new BufferedReader(reader)) {
            while (br.ready()) {
                lines.add(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        for (int i = 0; i < lines.size(); i++) {
            if (i < lines.size() - 2) {
                taskFromString(lines.get(i));
            } else if (i == lines.size() - 1) {
                historyFromString(lines.get(i));
            }
        }

    }

    private void taskFromString(String value) throws IllegalAccessException {
        if (value != null && !value.isEmpty() && !value.isBlank()) {
            String[] split = value.split(",");
            int id = Integer.parseInt(split[0]);
            TypeTask type = TypeTask.valueOf(split[1]);
            String name = split[2];
            StatusTask status = StatusTask.valueOf(split[3]);
            String description = split[4];
            switch (type) {
                case EPIC -> {
                    Epic epic = new Epic(name, description);
                    epic.setId(id);
                    epic.setStatus(status);
                    allEpic.put(epic.getId(), epic);
                }
                case SUBTASK -> {
                    int idOfEpic = Integer.parseInt(split[5]);
                    Subtask subtask = new Subtask(name, description, idOfEpic);
                    subtask.setId(id);
                    subtask.setStatus(status);
                    ArrayList<Integer> idForEpic = new ArrayList<>();
                    idForEpic.add(subtask.getId());
                    allEpic.get(idOfEpic).setListIdOfSubTask(idForEpic);
                    allSubTask.put(subtask.getId(), subtask);
                }
                case TASK -> {
                    Task task = new Task(name, description);
                    task.setId(id);
                    if (task.getId() == 0) {
                        task.setId(1);
                    }
                    task.setStatus(status);
                    allTask.put(task.getId(), task);
                }
                default -> throw new IllegalAccessException();
            }

        }

    }

    void historyFromString(String value) {
        String[] idOfHistory = value.split(",");
        List<Integer> idFromHistory = new ArrayList<>();
        int[] idOfTask = new int[idOfHistory.length];
        for (int i = 0; i < idOfHistory.length; i++) {
            if (!idOfHistory[i].equals("")) {
                idOfTask[i] = Integer.parseInt(idOfHistory[i]);
            }

        }
        for (int id : idOfTask) {
            if (allTask.containsKey(id)) {
                historyManager.add(allTask.get(id));
            } else if (allEpic.containsKey(id)) {
                historyManager.add(allEpic.get(id));
            } else {
                historyManager.add(allSubTask.get(id));
            }
        }
        for (String id : idOfHistory) {
            if (!id.equals("")) {
                idFromHistory.add(Integer.valueOf(id));
            }
        }
    }

}



