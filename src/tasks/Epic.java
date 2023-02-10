package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> listIdOfSubTask = new ArrayList<>();

    protected LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, StatusTask status, String name, String description) {
        super(id, status, name, description);
    }

    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    public ArrayList<Integer> getListIdOfSubTask() {
        return listIdOfSubTask;
    }

    public void setListIdOfSubTask(ArrayList<Integer> listIdOfSubTask) {
        this.listIdOfSubTask = listIdOfSubTask;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return listIdOfSubTask.equals(epic.listIdOfSubTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listIdOfSubTask);
    }

    @Override
    public String toString() {
        if (getDuration() != 0) {
            return "Id:" + getId() + " Name-" + getName() + "-" + getDescription() + " Статус " + getStatus()
                    + " " + "Список id подзадач " + listIdOfSubTask + " startTime:"
                    + getStartTime() + " продолжительность:" + getDuration() + " endTime:" + getEndTime();
        } else {
            return "Id:" + getId() + " Name-" + getName() + "-" + getDescription() + " Статус " + getStatus()
                    + " " + "Список id подзадач " + listIdOfSubTask;
        }
    }
}
