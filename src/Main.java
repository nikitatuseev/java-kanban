import managers.FileBackedTasksManager;
import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws InstantiationException {
        TaskManager manager = Managers.getDefault();
        TaskManager memory = Managers.saveInMemory();
        FileBackedTasksManager load = FileBackedTasksManager.loadFromFile(new File("resources/record"));
        System.out.println(load.getAllTask());
        System.out.println(load.getAllEpic());


        Task task = new Task("dd", "ss", LocalDateTime.now(), 30);
        memory.saveTask(task);

        System.out.println("LOOK");
        System.out.println(memory.getAllTask());
        Epic epic = new Epic("sd", "wd");
        memory.saveEpic(epic);

        Subtask subTask = new Subtask("gd", "wdw", epic.getId(), LocalDateTime.now().plusMinutes(30), 60);
        memory.saveSubTask(subTask);

        Subtask subtask2 = new Subtask("grfg", "efg", epic.getId(), LocalDateTime.now().plusMinutes(90), 60);
        memory.saveSubTask(subtask2);

        System.out.println(memory.getSortedTasks());
        /*
        Subtask subtask3 = new Subtask("okd", "jsidj", epic.getId(), LocalDateTime.now().plusMinutes(150), 20);
        manager.saveSubTask(subtask3);
        System.out.println(manager.getAllEpic());
        //manager.removeSubTaskById(3);
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());
        System.out.println("LKKK");
        Subtask newSubTask=new Subtask(subTask.getId(), StatusTask.DONE,"wdw","gd", epic.getId(), LocalDateTime.now().plusMinutes(200),30);
        manager.updateSubTask(newSubTask);
        System.out.println(manager.getSortedTasks());


         */

    }
}
