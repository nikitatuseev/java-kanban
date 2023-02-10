package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected StatusTask status = StatusTask.NEW;
    protected int id;

    protected long duration;

    protected LocalDateTime startTime;


    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task(int id, StatusTask status, String name, String description, LocalDateTime startTime, long duration) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, LocalDateTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, StatusTask status, String name, String description) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.description = description;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        Duration duration = Duration.ofMinutes(this.duration);
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name) && description.equals(task.description) && status.equals(task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    public String toString() {
        if (getDuration() != 0) {
            return "Id:" + getId() + " Name:" + getName() + "-" + getDescription() + " Статус-" + getStatus() + " startTime:"
                    + getStartTime() + " продолжительность:" + getDuration() + " endTime:" + getEndTime();
        } else {
            return "Id:" + getId() + " Name:" + getName() + "-" + getDescription() + " Статус-" + getStatus();
        }
    }
}