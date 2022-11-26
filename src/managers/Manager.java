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
        Epic nameEpic = allEpic.get(subtask.getIdOfEpic());
        ArrayList<Integer> list = nameEpic.getListIdOfSubTask();
        list.add(id);
        nameEpic.setListIdOfSubTask(list);
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
        ArrayList<Subtask> allEpic1 = new ArrayList<>();
        Epic nameEpic = allEpic.get(id);
        ArrayList<Integer> list = nameEpic.getListIdOfSubTask();
        for (int idOfSubTask : list) {
            allEpic1.add(allSubTask.get(idOfSubTask));
        }
        return allEpic1;

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
            Epic nameEpic = allEpic.get(key);
            ArrayList<Integer> list = nameEpic.getListIdOfSubTask();
            list.clear();
            nameEpic.setListIdOfSubTask(list);
            nameEpic.setStatus("NEW");//мне кажется что не надо вызывать метод обновления статуса
            // т.к если нет подзадач то у всех эпиков статус NEW
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
            Epic nameEpic = allEpic.get(id);
            ArrayList<Integer> list = nameEpic.getListIdOfSubTask();
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
            Epic nameEpic = allEpic.get(nameSubTask.getIdOfEpic());
            ArrayList<Integer> listId = nameEpic.getListIdOfSubTask();
            listId.remove(Integer.valueOf(id));
            nameEpic.setListIdOfSubTask(listId);
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
        Epic nameEpic = allEpic.get(subtask.getIdOfEpic());
        ArrayList<Integer> list = nameEpic.getListIdOfSubTask();
        for (Integer id : list) {
            Subtask statusOfSubTask = allSubTask.get(id);
            if (statusOfSubTask.getStatus().equals("DONE")) {
                statusDone++;
            } else if (statusOfSubTask.getStatus().equals("NEW")) {
                statusNew++;
            }
        }
        if (list.isEmpty()) {
            nameEpic.setStatus("NEW");
            return;
        }
        if (statusDone == list.size()) {
            nameEpic.setStatus("DONE");
        } else if (statusNew == list.size()) {
            nameEpic.setStatus("NEW");
        } else {
            nameEpic.setStatus("IN_PROGRESS");
        }
    }

    public void updateEpic(Epic epic) {
        if (allEpic.get(epic.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            Epic nameEpic = allEpic.get(epic.getId());
            nameEpic.setName(epic.getName());
            nameEpic.setDescription(epic.getDescription());
        }

    }

}

