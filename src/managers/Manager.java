package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    //мапа для обычный задач
    HashMap<Integer, Task> allTask = new HashMap<>();
    int id = 1;
    //мапа для эпиков
    HashMap<Integer, Epic> allEpic = new HashMap<>();
    //мапа для сабтасков
    HashMap<Integer, Subtask> allSubTask = new HashMap<>();

    public void saveTask(Task task) {
        task.setId(id);
        allTask.put(id, task);
        id++;

    }

    public void saveEpic(Epic epic) {
        epic.setId(id);
        allEpic.put((id), epic);
        id++;
    }

    public void saveSubTask(Subtask subtask) {
        subtask.setId(id);
        allSubTask.put(id, subtask);
        Epic epicForStatusCheck = allEpic.get(subtask.getIdOfEpic());
        ArrayList<Integer> list = epicForStatusCheck.getListIdOfSubTask();
        list.add(id);
        epicForStatusCheck.setListIdOfSubTask(list);
        id++;
        updateEpicStatus(subtask);
    }

    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(allTask.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<>(allEpic.values());
    }

    public ArrayList<Subtask> getAllSubTask() {
        return new ArrayList<>(allSubTask.values());
    }

    public Task getTaskById(int id) {
        return allTask.get(id);
    }

    public Epic getEpicById(int id) {
        return allEpic.get(id);
    }

    public Subtask getSubTaskById(int id) {
        return allSubTask.get(id);
    }

    public ArrayList<Subtask> getAllSubByNameEpic(int id) {
        ArrayList<Subtask> allEpicSubTask = new ArrayList<>();
        Epic desiredEpic = allEpic.get(id);
        ArrayList<Integer> list = desiredEpic.getListIdOfSubTask();
        for (int idOfSubTask : list) {
            allEpicSubTask.add(allSubTask.get(idOfSubTask));
        }
        return allEpicSubTask;

    }

    public void removeAllTask() {
        allTask.clear();
    }

    public void removeAllEpic() {
        allEpic.clear();
        allSubTask.clear();
    }

    public void removeAllSubTask() {
        allSubTask.clear();
        for (int key : allEpic.keySet()) {
            Epic allEpicWithSub = allEpic.get(key);
            ArrayList<Integer> list = allEpicWithSub.getListIdOfSubTask();
            list.clear();
            allEpicWithSub.setListIdOfSubTask(list);
            allEpicWithSub.setStatus("NEW");
        }
    }

    public void removeTaskById(int id) {
        if (allTask.get(id) == null) {
            System.out.println("Такой задачи нет");
        } else {
            allTask.remove(id);
        }
    }

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

    public void updateTask(Task task) {
        if (allTask.get(task.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            allTask.put(task.getId(), task);
        }
    }

    public void updateSubTask(Subtask subtask) {
        if (allSubTask.get(subtask.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            allSubTask.put(subtask.getId(), subtask);
            updateEpicStatus(subtask);
        }
    }

    void updateEpicStatus(Subtask subtask) {
        int statusDone = 0;
        int statusNew = 0;
        Epic epicForUpdate = allEpic.get(subtask.getIdOfEpic());
        ArrayList<Integer> list = epicForUpdate.getListIdOfSubTask();
        if (list.isEmpty()) {
            epicForUpdate.setStatus("NEW");
        } else {
            for (Integer id : list) {
                Subtask statusOfSubTask = allSubTask.get(id);
                if (statusOfSubTask.getStatus().equals("DONE")) {
                    statusDone++;
                } else if (statusOfSubTask.getStatus().equals("NEW")) {
                    statusNew++;
                }
            }
            if (statusDone == list.size()) {
                epicForUpdate.setStatus("DONE");
            } else if (statusNew == list.size()) {
                epicForUpdate.setStatus("NEW");
            } else {
                epicForUpdate.setStatus("IN_PROGRESS");
            }
        }
    }

    public void updateEpic(Epic epic) {
        if (allEpic.get(epic.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            Epic epicForUpdate = allEpic.get(epic.getId());
            epicForUpdate.setName(epic.getName());
            epicForUpdate.setDescription(epic.getDescription());
        }
    }
}

