package tasks;


public class Epic extends Tasks {
    public Epic(String nameAndDescription, String status) {
        super(nameAndDescription, status);
    }


    @Override
    public String toString() {
        return nameAndDescription + "=" + status;
    }

}
