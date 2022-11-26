package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> listIdOfSubTask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public ArrayList<Integer> getListIdOfSubTask() {
        return listIdOfSubTask;
    }

    public void setListIdOfSubTask(ArrayList<Integer> listIdOfSubTask) {
        this.listIdOfSubTask = listIdOfSubTask;
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
        return "Id:" + getId() + " Name-" + getName() + "-" + getDescription() + " Статус " + getStatus()
                + " " + "Список id подзадач " + listIdOfSubTask;
    }

}
