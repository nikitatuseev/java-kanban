package managers;

import exceptions.ManagerSaveException;
import tasks.*;


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

    private  final TreeSet<Task> sortTasks = new TreeSet<>((a, b) -> {
        if (a.getStartTime() == null) {
            return 1;
        }
        if (b.getStartTime() == null) {
            return -1;
        }
        return a.getStartTime().compareTo(b.getStartTime());
    });

    @Override
    public void saveTask(Task task) {
        task.setId(id);
        allTask.put(id, task);
        addTaskForSorting(task);
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
                epicDurationWithSaveSubTask(epicForStatusCheck, list);
            }
            allEpic.put(subtask.getIdOfEpic(), epicForStatusCheck);
            addTaskForSorting(subtask);
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
        sortTasks.removeIf(task -> TypeTask.SUBTASK == task.getType());
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
            epicDurationWithRemoveAll(allEpicWithSub);
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
                sortTasks.remove(getSubTaskById(keySubTask));
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
            epicDuration(epicWithSelectedSub, listId, id);
            if (allSubTask.containsKey(id)) {
                sortTasks.remove(allSubTask.get(id));
            }
            allSubTask.remove(id);
        }
    }

    public void epicDurationWithRemoveAll(Epic epic) {
        epic.setEndTime(null);
        epic.setDuration(0);
        epic.setStartTime(null);
    }
//использую этот метод для обновления subTask и для удаления subTask по id
    public void epicDuration(Epic epic, ArrayList<Integer> list, int id) {
        if (list.size() != 0) {
            epic.setListIdOfSubTask(list);
            epic.setDuration(epic.getDuration() - getSubTaskById(id).getDuration());
            epic.setStartTime(getSubTaskById(list.get(0)).getStartTime());
            epic.setEndTime(getSubTaskById(list.get(list.size() - 1)).getEndTime());
        } else {
            epic.setDuration(0);
        }

    }

    public void epicDurationWithSaveSubTask(Epic epic, ArrayList<Integer> list) {
        LocalDateTime startTimeEpic = getSubTaskById(list.get(0)).getStartTime();
        LocalDateTime endTimeEpic;
        if (list.size() > 1) {
            endTimeEpic = getSubTaskById(list.get(list.size() - 1)).getEndTime();
        } else {
            endTimeEpic = getSubTaskById(list.get(0)).getEndTime();
        }
        epic.setStartTime(startTimeEpic);
        epic.setEndTime(endTimeEpic);
        epic.setDuration(0);
        for (Integer integer : list) {
            epic.setDuration(epic.getDuration() +
                    getSubTaskById(integer).getDuration());
        }
    }

    @Override
    public void updateTask(Task task) {
        if (allTask.get(task.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            sortTasks.remove(allTask.get(task.getId()));
            allTask.put(task.getId(), task);
            addTaskForSorting(task);
        }
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        if (allSubTask.get(subtask.getId()) == null) {
            System.out.println("Такой задачи нет");
        } else {
            sortTasks.remove(allSubTask.get(subtask.getId()));
            allSubTask.put(subtask.getId(), subtask);
            addTaskForSorting(subtask);
            ArrayList<Integer> list = getEpicById(subtask.getIdOfEpic()).getListIdOfSubTask();
            epicDuration(getEpicById(subtask.getIdOfEpic()), list, subtask.getId());
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

    public void addTaskForSorting(Task task){
        checkIntersections(task);
        sortTasks.add(task);
    }

    @Override
    public TreeSet<Task> getSortedTasks() {
        return sortTasks;
    }

    public  void checkIntersections(Task task){
        if(sortTasks.size()!=0){
            for(Task tasks:sortTasks){
               if( tasks.getEndTime().isAfter(task.getStartTime())||
                       task.getStartTime().isBefore(tasks.getEndTime())){
                   throw new ManagerSaveException("Найдено пересечение" + tasks+ " и " + task);
               }
            }
        }
    }
}
