package tasks;

import java.util.Objects;

public class Subtask extends Task {
    private int idOfEpic;

    public Subtask(String name, String description, int idOfEpic) {
        super(name, description);
        this.idOfEpic = idOfEpic;
    }
    //использую этот конструктор для обновления
    public Subtask(String name, String description, int idOfEpic, String status, int id) {
        super(name, description, status, idOfEpic);
        this.setStatus(status);
        this.id = id;
        this.idOfEpic = idOfEpic;
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
