package tasks;


public class Tasks {
    public String nameAndDescription;
    public String status;


    public Tasks(String nameAndDescription, String status) {
        this.nameAndDescription = nameAndDescription;
        this.status = status;

    }

    @Override
    public String toString() {
        return nameAndDescription + "= " + status;
    }
}