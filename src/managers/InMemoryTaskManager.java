package managers;

import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager = Managers.getDefaultHistory();//меня сильно смущает эта строчка
    //я не уверен можно ли так делать
    private final HashMap<Integer, Task> allTask = new HashMap<>();
    private int id = 1;
    //мапа для эпиков
    private final HashMap<Integer, Epic> allEpic = new HashMap<>();
    //мапа для сабтасков
    private final HashMap<Integer, Subtask> allSubTask = new HashMap<>();

    private void createId() {
        id++;
    }

    @Override
    public void saveTask(Task task) {
        task.setId(id);
        allTask.put(id, task);
        createId();
    }

    @Override
    public void saveEpic(Epic epic) {
        epic.setId(id);
        allEpic.put((id), epic);
        createId();
    }

    @Override
    public void saveSubTask(Subtask subtask) {
        subtask.setId(id);
        allSubTask.put(id, subtask);
        Epic epicForStatusCheck = allEpic.get(subtask.getIdOfEpic());
        ArrayList<Integer> list = epicForStatusCheck.getListIdOfSubTask();
        list.add(id);
        epicForStatusCheck.setListIdOfSubTask(list);
        createId();
        updateEpicStatus(subtask);
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(allTask.values());
    }

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<>(allEpic.values());
    }

    @Override
    public List<Subtask> getAllSubTask() {
        return new ArrayList<>(allSubTask.values());
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(allTask.get(id));
        return allTask.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(allEpic.get(id));
        return allEpic.get(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        historyManager.add(allSubTask.get(id));
        return allSubTask.get(id);
    }

    @Override
    public List<Subtask> getAllSubByNameEpic(int id) {
        ArrayList<Subtask> allEpicSubTask = new ArrayList<>();
        Epic desiredEpic = allEpic.get(id);
        ArrayList<Integer> list = desiredEpic.getListIdOfSubTask();
        for (int idOfSubTask : list) {
            allEpicSubTask.add(allSubTask.get(idOfSubTask));
        }
        return allEpicSubTask;

    }

    @Override
    public void removeAllTask() {
        allTask.clear();
    }

    @Override
    public void removeAllEpic() {
        allEpic.clear();
        allSubTask.clear();
    }

    @Override
    public void removeAllSubTask() {
        allSubTask.clear();
        for (int key : allEpic.keySet()) {
            Epic allEpicWithSub = allEpic.get(key);
            ArrayList<Integer> list = allEpicWithSub.getListIdOfSubTask();
            list.clear();
            allEpicWithSub.setListIdOfSubTask(list);
            allEpicWithSub.setStatus(StatusTask.NEW);
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (allTask.get(id) == null) {
            System.out.println("Такой задачи нет");
        } else {
            allTask.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        if (allEpic.get(id) == null) {
            System.out.println("Такой задачи нет");
        } else {
            Epic selectedEpic = allEpic.get(id);
            ArrayList<Integer> list = selectedEpic.getListIdOfSubTask();
            for (int keySubTask : list) {
                allSubTask.remove(keySubTask);
            }
            allEpic.remove(id);
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        if (allSubTask.get(id) == null) {
            System.out.println("Такой задачи нет");
        } else {
            Subtask nameSubTask = allSubTask.get(id);
            Epic epicWithSelectedSub = allEpic.get(nameSubTask.getIdOfEpic());
            ArrayList<Integer> listId = epicWithSelectedSub.getListIdOfSubTask();
            listId.remove(Integer.valueOf(id));
            epicWithSelectedSub.setListIdOfSubTask(listId);
            allSubTask.remove(id);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (allTask.get(task.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            allTask.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        if (allSubTask.get(subtask.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            allSubTask.put(subtask.getId(), subtask);
            updateEpicStatus(subtask);
        }
    }

    @Override
    public void updateEpicStatus(Subtask subtask) {
        int statusDone = 0;
        int statusNew = 0;
        Epic epicForUpdate = allEpic.get(subtask.getIdOfEpic());
        ArrayList<Integer> list = epicForUpdate.getListIdOfSubTask();
        if (list.isEmpty()) {
            epicForUpdate.setStatus(StatusTask.NEW);
        } else {
            for (Integer id : list) {
                Subtask statusOfSubTask = allSubTask.get(id);
                if (statusOfSubTask.getStatus().equals(StatusTask.DONE)) {
                    statusDone++;
                } else if (statusOfSubTask.getStatus().equals(StatusTask.NEW)) {
                    statusNew++;
                }
            }
            if (statusDone == list.size()) {
                epicForUpdate.setStatus(StatusTask.DONE);
            } else if (statusNew == list.size()) {
                epicForUpdate.setStatus(StatusTask.NEW);
            } else {
                epicForUpdate.setStatus(StatusTask.In_Progress);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (allEpic.get(epic.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            Epic epicForUpdate = allEpic.get(epic.getId());
            epicForUpdate.setName(epic.getName());
            epicForUpdate.setDescription(epic.getDescription());
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
