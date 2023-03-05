package managers;

import exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
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
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubTaskById(int id) {
        Subtask subTask = super.getSubTaskById(id);
        save();
        return subTask;
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
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    protected void save() {
        final String historyInString = historyToString(historyManager);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,status,name,description,startTime,duration,endTime");
            fileWriter.write("\n");
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
            throw new ManagerSaveException("данные не сохранены");
        }
    }

    private String taskToString(Task task) {
        if (task.getType() == TypeTask.SUBTASK) {
            return SubTaskToString((Subtask) task);
        } else {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription()
                    + "," + task.getStartTime() + "," + task.getDuration() + "," + task.getEndTime();
        }
    }

    private String SubTaskToString(Subtask task) {
        return task.getId() + "," + "SUBTASK" + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + task.getIdOfEpic()
                + "," + task.getStartTime() + "," + task.getDuration() + "," + task.getEndTime();
    }

    private String historyToString(HistoryManager historyManager) {
        List<Integer> taskId = new ArrayList<>();
        for (Task task : historyManager.getHistory()) {
            taskId.add(task.getId());
        }
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

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager loadFromFile = new FileBackedTasksManager(file);
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                lines.add(br.readLine());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения");
        }
        for (int i = 1; i < lines.size(); i++) {
            if (i < lines.size() - 2) {
                String[] split = loadFromFile.taskFromString(lines.get(i));
                int id = Integer.parseInt(split[0]);
                TypeTask type = TypeTask.valueOf(split[1]);
                String name = split[2];
                StatusTask status = StatusTask.valueOf(split[3]);
                String description = split[4];
                LocalDateTime startTime;
                long duration;
                LocalDateTime endTime = null;
                if (type == TypeTask.SUBTASK) {
                    startTime = LocalDateTime.parse(split[6]);
                    duration = Long.parseLong((split[7]));
                } else {
                    startTime = LocalDateTime.parse(split[5]);
                    duration = Long.parseLong((split[6]));
                    endTime = LocalDateTime.parse(split[7]);
                }
                if (type == TypeTask.EPIC) {
                    Epic epic = new Epic(id, status, name, description);
                    epic.setStartTime(startTime);
                    epic.setDuration(duration);
                    epic.setEndTime(endTime);
                    loadFromFile.allEpic.put(epic.getId(), epic);
                } else if (type == TypeTask.SUBTASK) {
                    int idOfEpic = Integer.parseInt(split[5]);
                    Subtask subtask = new Subtask(id, status, name, description, idOfEpic, startTime, duration);
                    ArrayList<Integer> idForEpic = loadFromFile.allEpic.get(idOfEpic).getListIdOfSubTask();
                    idForEpic.add(subtask.getId());
                    loadFromFile.allEpic.get(idOfEpic).setListIdOfSubTask(idForEpic);
                    loadFromFile.allSubTask.put(subtask.getId(), subtask);
                    loadFromFile.addTaskForSorting(subtask);
                } else if (type == TypeTask.TASK) {
                    Task task = new Task(id, status, name, description, startTime, duration);
                    if (task.getId() == 0) {
                        task.setId(1);
                    }
                    task.setStatus(status);
                    loadFromFile.allTask.put(task.getId(), task);
                    loadFromFile.addTaskForSorting(task);
                }
            } else if (i == lines.size() - 1) {
                historyFromString(lines.get(i));
                List<Integer> idOfHistory = historyFromString(lines.get(i));
                for (int id : idOfHistory) {
                    if (loadFromFile.allTask.containsKey(id)) {
                        loadFromFile.historyManager.add(loadFromFile.allTask.get(id));
                    } else if (loadFromFile.allEpic.containsKey(id)) {
                        loadFromFile.historyManager.add(loadFromFile.allEpic.get(id));
                    } else {
                        loadFromFile.historyManager.add(loadFromFile.allSubTask.get(id));
                    }
                }
            }
        }
        return loadFromFile;
    }

    private String[] taskFromString(String value) {
        String[] split = new String[0];
        if (value != null && !value.isBlank()) {
            split = value.split(",");
        }
        return split;
    }

    static List<Integer> historyFromString(String value) {
        String[] idOfHistory = value.split(",");
        List<Integer> idFromHistory = new ArrayList<>();
        for (String id : idOfHistory) {
            idFromHistory.add(Integer.valueOf(id));
        }
        return idFromHistory;
    }
}