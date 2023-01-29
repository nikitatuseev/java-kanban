package tasks;

import java.util.Objects;

public class Subtask extends Task {
    private int idOfEpic;

    public Subtask(String name, String description, int idOfEpic) {
        super(name, description);
        this.idOfEpic = idOfEpic;
    }

    public Subtask(int id, StatusTask status, String name, String description, int idOfEpic) {
        super(id, status, name, description);
        this.idOfEpic = idOfEpic;
    }

    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    public int getIdOfEpic() {
        return idOfEpic;
    }

    public void setIdOfEpic(int idOfEpic) {
        this.idOfEpic = idOfEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idOfEpic == subtask.idOfEpic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idOfEpic);
    }

    @Override
    public String toString() {
        return "Id:" + getId() + " Name:" + getName() + "-" + getDescription() + " Статус-" + getStatus()
                + " id Эпика=" + getIdOfEpic();
    }
}
