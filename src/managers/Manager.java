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
    HashMap<Integer, Object> allEpic = new HashMap<Integer, Object>();

    //мапа для сабтасков
    HashMap<Integer, Subtask> allSubTask = new HashMap<>();

    public void saveTask(Task task) {
        task.setId(id);
        allTask.put((id), task);
        id += 1;

    }

    public void saveEpic(Epic epic) {
        epic.setId(id);
        allEpic.put((id), epic);
        id += 1;
    }

    HashMap<Integer, Integer> number = new HashMap<>();

    public void saveSubTask(Subtask subtask) {
        subtask.setId(id);
        allSubTask.put(id, subtask);
        number.put(id, subtask.getIdOfEpic());
        id += 1;
    }


    public ArrayList<String> getAllTask() {
        if (allTask.isEmpty()) {
            System.out.println("Задач нет");
        }
        ArrayList<Integer> keyTask = new ArrayList<>();
        ArrayList<String> allTasks = new ArrayList<>();
        for (int key : allTask.keySet()) {
            keyTask.add(key);
        }
        for (int i = 0; i < keyTask.size(); i++) {
            allTasks.add(String.valueOf(allTask.get(keyTask.get(i))));
        }

        return allTasks;
    }

    public ArrayList<String> getAllEpic() {
        if (allEpic.isEmpty()) {
            System.out.println("Эпиков нет");
        }
        ArrayList<Integer> keyEpic = new ArrayList<>();
        ArrayList<String> listAllEpic = new ArrayList<>();
        for (int key : allEpic.keySet()) {
            keyEpic.add(key);
        }
        for (int i = 0; i < keyEpic.size(); i++) {
            listAllEpic.add(String.valueOf(allEpic.get(keyEpic.get(i))));
        }

        return listAllEpic;
    }

    public ArrayList<String> getAllSubTask() {
        if (allSubTask.isEmpty()) {
            System.out.println("Сабтасков нет");
        }
        ArrayList<Integer> keySubTask = new ArrayList<>();
        ArrayList<String> listAllSubTask = new ArrayList<>();
        for (int key : allSubTask.keySet()) {
            keySubTask.add(key);
        }
        for (int i = 0; i < keySubTask.size(); i++) {
            listAllSubTask.add(String.valueOf(allSubTask.get(keySubTask.get(i))));
        }

        return listAllSubTask;
    }


    public Task getTaskById(int id) {
        return allTask.get(id);
    }

    public Object getEpicById(int id) {
        return allEpic.get(id);
    }

    public Subtask getSubTaskById(int id) {
        return allSubTask.get(id);
    }

    public void getAllSubByNameEpic(String nameEpic) {
        ArrayList<Integer> keySub = new ArrayList<>();

        for (Integer key : allSubTask.keySet()) {

            keySub.add(key);

        }
        for (int i = 0; i < allEpic.size(); i++) {

            if (allSubTask.get(keySub.get(i)).equals(nameEpic)) {
                System.out.println(keySub.get(i));
            }

        }
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
    }

    public void removeTaskById(int id) {
        allTask.remove(id);
    }

    public void removeEpicById(int id) {
        ArrayList<Integer> listKey = new ArrayList<>();
        allEpic.remove(id);
        for (int key : number.keySet()) {
            listKey.add(key);
        }
        for (int i = 0; i < listKey.size(); i++) {
            if (number.get(listKey.get(i)) == id) {
                allSubTask.remove(listKey.get(i));
                number.remove(i);
            }
        }
    }

    public void removeSubTaskById(int id) {
        allSubTask.remove(id);
    }

    public void updateTask(Task task) {
        allTask.put(task.getId(), task);
    }

    public void updateSubTask(Subtask subtask) {
        allSubTask.put(subtask.getId(), subtask);
    }

}

