package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    //мапы для обычный задач
    ArrayList<Tasks> newTask = new ArrayList<>();
    HashMap<Integer, Tasks> allTask = new HashMap<>();


    //мапы для эпиков
    ArrayList<Epic> newEpic = new ArrayList<>();
    HashMap<Integer, Epic> allEpic = new HashMap<>();


    //мапы для сабтасков
    ArrayList<Subtask> newSub = new ArrayList<>();
    HashMap<Integer, Subtask> allSub = new HashMap<>();
    HashMap<String, String> sub = new HashMap<String, String>();
    ArrayList<String> nameOfEpic = new ArrayList<>();


    public HashMap<Integer, Tasks> saveTask(Tasks tasks) {
        newTask.add(tasks);
        for (int i = 0; i < newTask.size(); i++) {
            allTask.put((i + 1), newTask.get(i));  //ключ-id значение-задачи

        }
        return allTask;
    }

    @Override
    public String toString() {
        return String.valueOf(newTask);
    }

    public HashMap<Integer, Epic> saveEpic(Epic epic) {
        newEpic.add(epic);
        for (int i = 0; i < newEpic.size(); i++) {
            allEpic.put((i + 1 + newTask.size()), newEpic.get(i));

        }

        return allEpic;
    }

    public HashMap<Integer, Subtask> saveSub(Subtask subtask, String nameEpic) {
        newSub.add(subtask);
        nameOfEpic.add(nameEpic);
        for (int i = 0; i < newSub.size(); i++) {
            sub.put(String.valueOf(newSub.get(i)), nameOfEpic.get(i));
        }
        for (int i = 0; i < newSub.size(); i++) {
            allSub.put((i + 1 + (newEpic.size() + newTask.size())), newSub.get(i));


        }

        return allSub;
    }


    public ArrayList<String> getAllTask() {
        if (newTask.isEmpty()) {
            System.out.println("Задач нет");
        }
        ArrayList<String> allTasks = new ArrayList<>();
        ArrayList<Integer> keySet = new ArrayList<>();
        for (Integer key : allSub.keySet()) {
            keySet.add(key);
        }
        for (int i = 0; i < (allSub.size() - 1); i++) {
            if (allSub.get(keySet.get(i)) == allSub.get(keySet.get(i + 1))) {
                allSub.remove(keySet.get(i + 1));
            }
        }
        allTasks.add(String.valueOf(allTask));
        allTasks.add(String.valueOf(allEpic));
        allTasks.add(String.valueOf(allSub));
        return allTasks;
    }

    public void getTask(int id) {
        System.out.println("значение id " + id + ":");
        if (allTask.get(id) == null) {
            if (allSub.get(id) == null) {
                System.out.println(allEpic.get(id));
            } else {
                System.out.println(allSub.get(id));
            }
        } else {
            System.out.println(allTask.get(id));
        }

    }

    public void getAllSub(String nameEpic) {
        ArrayList<String> keySub = new ArrayList<>();
        for (String key : sub.keySet()) {
            keySub.add(key);
        }
        for (int i = 0; i < sub.size(); i++) {
            if (sub.get(keySub.get(i)).equals(nameEpic)) {
                System.out.println(keySub.get(i));
            }
        }
    }

    public void removeEpicById(int id) {
        ArrayList<String> keySubForSub = new ArrayList<>();
        ArrayList<Integer> keysubForAllSub = new ArrayList<>();
        for (int i = 0; i < allSub.size(); i++) {
            for (String keys : sub.keySet()) {
                keySubForSub.add(keys);
            }

        }
        for (Integer nkeys : allSub.keySet()) {
            keysubForAllSub.add(nkeys);
        }

        String nameEp = String.valueOf(allEpic.get(id));
        String[] stringForSplit = nameEp.split("=");
        for (int i = 0; i < sub.size(); i++) {
            for (int m = 0; m < 2; m++) {
                if (sub.get(keySubForSub.get(i)).equals(stringForSplit[m])) {
                    allSub.remove(keysubForAllSub.get(i));
                    sub.remove(allSub.get(id));
                }

            }
        }
        allEpic.remove(id);
    }

    public void removeAllTask() {
        allTask.clear();
        newTask.clear();

        allEpic.clear();

        newEpic.clear();
        allSub.clear();

        newSub.clear();
        System.out.println("Задачи удалены");
    }

    public void removeById(int id) {
        if (allTask.get(id) == null) {
            if (allSub.get(id) == null) {
                allEpic.remove(id);
                allSub.remove(id);

                for (int i = 0; i < newSub.size(); i++) {
                    if (newSub.get(i) == allSub.get(id)) {
                        newSub.remove(i);
                    }
                }
                for (int i = 0; i < newEpic.size(); i++) {
                    if (newEpic.get(i) == allEpic.get(id)) {
                        newEpic.remove(i);
                    }
                }
            } else {
                allSub.remove(id);

                for (int i = 0; i < newSub.size(); i++) {
                    if (newSub.get(i) == allSub.get(id)) {
                        newSub.remove(i);
                    }
                }
            }

        } else {
            allTask.remove(id);


            for (int i = 0; i < newTask.size(); i++) {
                if (newTask.get(i) == allTask.get(id)) {
                    newTask.remove(i);
                }
            }
        }
    }

    public void updateTask(int id, Tasks tasks) {

        for (int i = 0; i < newTask.size(); i++) {
            if (newTask.get(i) == allTask.get(id)) {
                newTask.remove(i);
            }
        }

        newTask.add(tasks);
        allTask.put(id, tasks);

    }

    public void updateSubTask(int id, Subtask subtask) {

        String stringAllSub = String.valueOf(allSub.get(id));

        String valueSub;
        valueSub = (sub.get(stringAllSub));//добавляет имя эпика обновляемого саба
        sub.remove(stringAllSub);
        sub.put(String.valueOf(subtask), valueSub);
        for (int i = 0; i < newSub.size(); i++) {
            if (newSub.get(i) == allSub.get(id)) {
                newSub.remove(i);
            }
        }
        newSub.add(subtask);
        allSub.put(id, subtask);
        ArrayList<String> stususSub = new ArrayList<>();
        for (int i = 0; i < newSub.size(); i++) {
            String val = String.valueOf(newSub.get(i));
            if (sub.get(val) == valueSub) {
                String valueInString = String.valueOf(newSub.get(i));
                String onlyStatus = valueInString.substring(valueInString.lastIndexOf("=") + 1);
                stususSub.add(onlyStatus);
            }
        }
        int k = 0;//счетчик для проверки статусов сабов определнного эпика
        int m = 0;
        for (int i = 0; i < stususSub.size(); i++) {
            if (stususSub.get(i).equals(" DONE")) {
                k += 1;
            }
            if (stususSub.get(i).equals(" In_Progress")) {
                m += 1;
            }
        }
        if (k == stususSub.size()) {
            String forStatusNEW = valueSub + "=NEW";
            String StatusEpic;
            String forStatusInProg = valueSub + "=In_Progress";
            for (int i = 0; i < newEpic.size(); i++) {
                StatusEpic = String.valueOf(newEpic.get(i));
                if (forStatusNEW.equals(StatusEpic) || forStatusInProg.equals(StatusEpic)) {
                    Epic epic = new Epic(valueSub, "DONE");
                    int forCycleStart = newEpic.size() + allEpic.size();
                    int forCycleEnd = forCycleStart * 2;
                    for (int j = forCycleStart; j < forCycleEnd; j++) {
                        if (allEpic.get(j) == newEpic.get(i)) {
                            allEpic.put(j, epic);
                        }
                    }
                    newEpic.remove(i);
                    newEpic.add(epic);
                }
            }

        } else if ((k < stususSub.size() && k != 0) || m != 0) {
            String forStatNEW = valueSub + "=NEW";
            String StatEpic;
            for (int i = 0; i < newEpic.size(); i++) {
                StatEpic = String.valueOf(newEpic.get(i));
                if (forStatNEW.equals(StatEpic)) {
                    Epic epic = new Epic(valueSub, "In_Progress");
                    int forCycleStart = newEpic.size() + allEpic.size();
                    int forCycleEnd = forCycleStart * 2;
                    for (int j = forCycleStart; j < forCycleEnd; j++) {
                        if (allEpic.get(j) == newEpic.get(i)) {
                            allEpic.put(j, epic);
                        }
                    }
                    newEpic.remove(i);
                    newEpic.add(epic);
                }
            }
        }
    }
}

    
