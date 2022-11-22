import managers.Manager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Tasks;


public class Main {
    // Я очень сильно запутался в тз и мне кажется что вообще все неправильно сделал
    public static void main(String[] args) {
        Manager manager = new Manager();

        Tasks taskFirst = new Tasks("таск1- описание1", "NEW");

        manager.saveTask(taskFirst);

        Tasks taskSecond = new Tasks("таск2- описание2", "NEW");
        manager.saveTask(taskSecond);

        Tasks tasksThird = new Tasks("таск3- описание3", "NEW");
        manager.saveTask(tasksThird);

        Epic epicFirst = new Epic("первый эпик", "NEW");
        manager.saveEpic(epicFirst);

        Epic epicSecond = new Epic("второй эпик", "NEW");
        manager.saveEpic(epicSecond);

        System.out.println(manager.getAllTask());

        Tasks newTasksThird = new Tasks("newtask3", "DONE");//поменять
        manager.updateTask(3, newTasksThird);
        System.out.println(manager.getAllTask());

        Subtask subtaskFirst = new Subtask("саб1", "NEW");
        manager.saveSub(subtaskFirst, "первый эпик");
        Subtask subtaskSecond = new Subtask("саб2", "NEW");
        manager.saveSub(subtaskSecond, "второй эпик");

        Subtask subtaskThird = new Subtask("саб3", "NEW");
        manager.saveSub(subtaskThird, "второй эпик");
        System.out.println(manager.getAllTask());
        Subtask newSubTaskThird = new Subtask("саб3", "DONE");
        manager.updateSubTask(8, newSubTaskThird);
        manager.saveSub(newSubTaskThird, "второй эпик");
        Subtask newSubTaskSecond = new Subtask("саб2", "DONE");
        manager.updateSubTask(7, newSubTaskSecond);

        Subtask newSubTaskFirst = new Subtask("саб1", "In_Progress");
        manager.updateSubTask(6, newSubTaskFirst);

        System.out.println(manager.getAllTask());


        manager.getAllSub("второй эпик");
        System.out.println();
        manager.getAllSub("первый эпик");
    }
}
