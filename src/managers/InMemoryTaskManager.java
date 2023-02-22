package managers;

import exceptions.ManagerSaveException;
import tasks.*;


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

    private final TreeSet<Task> sortTasks = new TreeSet<>((a, b) -> {
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
                epicDuration(epicForStatusCheck);
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
            epicDuration(allEpicWithSub);
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
            epicDuration(epicWithSelectedSub);
            if (allSubTask.containsKey(id)) {
                sortTasks.remove(allSubTask.get(id));
            }
            allSubTask.remove(id);
        }
    }

    public void epicDuration(Epic epic) {
        List<Task> sub = new ArrayList<>();
        for (Integer id : epic.getListIdOfSubTask()) {
            sub.add(allSubTask.get(id));
        }
        long duration = 0;
        epic.setStartTime(null);
        epic.setEndTime(null);
        for (Task subtask : sub) {
            if (epic.getStartTime() == null || subtask.getStartTime().isBefore(epic.getStartTime())) {
                epic.setStartTime(subtask.getStartTime());
            }
            if (epic.getEndTime() == null || subtask.getEndTime().isAfter(epic.getEndTime())) {
                epic.setEndTime(subtask.getEndTime());

            }
            duration += subtask.getDuration();
            epic.setDuration(duration);
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
            epicDuration(getEpicById(subtask.getIdOfEpic()));
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

    public void addTaskForSorting(Task task) {
        checkIntersections(task);
        sortTasks.add(task);
    }

    @Override
    public List<Task> getSortedTasks() {
        return new ArrayList<>(sortTasks);
    }

    //не очень понял в чем была ошибка прошлого варианта, ведь он и так сравнивал новую задачу со всеми уже имеющимися
    //в списке
    public void checkIntersections(Task newTask) {
        if (sortTasks.size() != 0) {
            for (Task task : sortTasks) {
                if (newTask.getStartTime().isBefore(task.getEndTime()) &&
                        newTask.getEndTime().isAfter(task.getStartTime())) {
                    throw new ManagerSaveException("Найдено пересечение" + task + " и " + newTask);
                }
            }
        }
    }
}
