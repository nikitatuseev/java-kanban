package tasks;


public class Subtask extends Tasks {


    public Subtask(String nameAndDescription, String status) {
        super(nameAndDescription, status);
    }


    @Override
    public String toString() {
        return nameAndDescription + "= " + status;
    }

}
