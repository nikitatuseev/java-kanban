package managers;

import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

import javax.lang.model.type.IntersectionType;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected HashMap<Integer, Task> allTask = new HashMap<>();
    protected int id = 1;
    protected HashMap<Integer, Epic> allEpic = new HashMap<>();
    protected HashMap<Integer, Subtask> allSubTask = new HashMap<>();

    private void createId() {
        id++;
    }

    @Override
    public void saveTask(Task task) {
        task.setId(id);
        allTask.put(id, task);
        try {
            addTaskForSorting(task);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
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
        if (allEpic.get(subtask.getIdOfEpic()) != null) {
            subtask.setId(id);
            allSubTask.put(id, subtask);
            Epic epicForStatusCheck = allEpic.get(subtask.getIdOfEpic());
            ArrayList<Integer> list = epicForStatusCheck.getListIdOfSubTask();
            list.add(id);
            epicForStatusCheck.setListIdOfSubTask(list);
            if (subtask.getDuration() != 0) {
                LocalDateTime startTimeEpic = getSubTaskById(list.get(0)).getStartTime();
                LocalDateTime endTimeEpic;
                if (list.size() > 1) {
                    endTimeEpic = getSubTaskById(list.get(list.size() - 1)).getEndTime();
                } else {
                    endTimeEpic = getSubTaskById(list.get(0)).getEndTime();
                }
                epicForStatusCheck.setStartTime(startTimeEpic);
                epicForStatusCheck.setEndTime(endTimeEpic);
                epicForStatusCheck.setDuration(0);
                for (Integer integer : list) {
                    epicForStatusCheck.setDuration(epicForStatusCheck.getDuration() +
                            getSubTaskById(integer).getDuration());
                }
            }
            allEpic.put(subtask.getIdOfEpic(), epicForStatusCheck);
            try {
                addTaskForSorting(subtask);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
            createId();
            updateEpicStatus(subtask);
        }

    }

    @Override
    public ArrayList<Task> getAllTask() {
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
    public List<Subtask> getSubtasksByNameEpic(int id) {
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
        for (int i : allTask.keySet()) {
            if (historyManager.getHistory().contains(allTask.get(i))) {
                historyManager.remove(i);
            }
            sortTasks.remove(allTask.get(i));
        }
        allTask.clear();
    }

    @Override
    public void removeAllEpic() {
        for (int i : allEpic.keySet()) {
            if (historyManager.getHistory().contains(allEpic.get(i))) {
                historyManager.remove(i);
            }
            for (int j : allEpic.get(i).getListIdOfSubTask()) {
                if (historyManager.getHistory().get(j) == allSubTask.get(i)) {
                    historyManager.remove(j);
                }
            }
        }
        allEpic.clear();
        allSubTask.clear();
    }

    @Override
    public void removeAllSubTask() {
        for (int i : allSubTask.keySet()) {
            if (historyManager.getHistory().contains(allSubTask.get(i))) {
                historyManager.remove(i);
            }
            sortTasks.remove(allSubTask.get(i));
        }
        allSubTask.clear();
        for (int key : allEpic.keySet()) {
            Epic allEpicWithSub = allEpic.get(key);
            ArrayList<Integer> list = allEpicWithSub.getListIdOfSubTask();
            list.clear();
            allEpicWithSub.setListIdOfSubTask(list);
            allEpicWithSub.setStatus(StatusTask.NEW);
            allEpicWithSub.setDuration(0);
        }
    }

    @Override
    public void removeTaskById(int id) {
        if (historyManager.getHistory().contains(allTask.get(id))) {
            historyManager.remove(id);
        }
        if (allTask.containsKey(id)) {
            sortTasks.remove(allTask.get(id));
        }
        allTask.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (allEpic.get(id) != null) {
            if (historyManager.getHistory().contains(allEpic.get(id))) {
                historyManager.remove(id);
            }
            if (allEpic.get(id).getListIdOfSubTask() != null) {
                for (int i : allEpic.get(id).getListIdOfSubTask()) {
                    if (historyManager.getHistory().contains(allSubTask.get(i))) {
                        historyManager.remove(i);
                    }
                }
            }

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
        if (allSubTask.get(id) != null) {
            if (historyManager.getHistory().contains(allSubTask.get(id))) {
                historyManager.remove(id);
            }
            Subtask nameSubTask = allSubTask.get(id);
            Epic epicWithSelectedSub = allEpic.get(nameSubTask.getIdOfEpic());
            ArrayList<Integer> listId = epicWithSelectedSub.getListIdOfSubTask();
            listId.remove(Integer.valueOf(id));
            if (listId.size() != 0) {
                epicWithSelectedSub.setListIdOfSubTask(listId);
                epicWithSelectedSub.setDuration(epicWithSelectedSub.getDuration() - getSubTaskById(id).getDuration());
                epicWithSelectedSub.setStartTime(getSubTaskById(listId.get(0)).getStartTime());
                epicWithSelectedSub.setEndTime(getSubTaskById(listId.get(listId.size() - 1)).getEndTime());
            } else {
                epicWithSelectedSub.setDuration(0);
            }
            if (allSubTask.containsKey(id)) {
                sortTasks.remove(allSubTask.get(id));
            }
            allSubTask.remove(id);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (allTask.get(task.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            sortTasks.remove(allTask.get(task.getId()));
            allTask.put(task.getId(), task);
            try {
                addTaskForSorting(task);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        if (allSubTask.get(subtask.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            sortTasks.remove(allSubTask.get(subtask.getId()));
            allSubTask.put(subtask.getId(), subtask);
            try {
                addTaskForSorting(subtask);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
            updateEpicStatus(subtask);
        }
    }

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
                epicForUpdate.setStatus(StatusTask.IN_PROGRESS);
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

    private static final TreeSet<Task> sortTasks = new TreeSet<>((a, b) -> {
        if (a.getStartTime() == null) {
            return 1;
        }
        if (b.getStartTime() == null) {
            return -1;
        }
        return a.getStartTime().compareTo(b.getStartTime());
    });

    //не уверен, что правильно обработал случаи когда есть пересечение
    public static void addTaskForSorting(Task task) throws InstantiationException {
        checkIntersections();
        sortTasks.add(task);
    }

    @Override
    public TreeSet<Task> getSortedTasks() {
        return sortTasks;
    }

    //сначала пытался сделать первым способом, но из-за него тесты не работали, а во втором я не знаю верную ли ошибку
    //обработал или вообще надо создавать свою собственную
    public static void checkIntersections() throws InstantiationException {
        /*Task prevTask=sortTasks.floor(task);
        if(prevTask!=null && prevTask.getEndTime().compareTo(task.getStartTime())>0){
            throw new InstantiationException("Найдено пересечение" + prevTask + " и " + task);
        }
        Task nextTask=sortTasks.ceiling(task);
        if(nextTask!=null&& nextTask.getStartTime().compareTo(task.getEndTime())<0){
            throw new InstantiationException("Найдено пересечение" + nextTask + " и " + task);
        }

         */
        List<Task> list = new ArrayList<>(sortTasks);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getStartTime().isBefore(list.get(i - 1).getEndTime())) {
                throw new InstantiationException("Найдено пересечение" + list.get(i - 1) + " и " + list.get(i));
            }
        }
    }

}
