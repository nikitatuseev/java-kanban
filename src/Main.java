import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws InstantiationException {
        //TaskManager manager = Managers.getDefault();
        /*TaskManager memory = Managers.saveInMemory();
        File file = new File(("resources/record"));
        FileBackedTasksManager fr = FileBackedTasksManager.loadFromFile(file);
        System.out.println(fr.getAllTask());
        System.out.println(fr.getAllEpic());
        System.out.println(fr.getAllSubTask());
        System.out.println(fr.getHistory());



        Task task = new Task(1, StatusTask.NEW, "task1", "description",LocalDateTime.of(1,1,1,1,0),30);
        memory.saveTask(task);
        Epic epic = new Epic(2, StatusTask.NEW, "epic1", "описание");
        memory.saveEpic(epic);
        Subtask subtask1 = new Subtask(3, StatusTask.NEW, "sub1", "description", epic.getId(),LocalDateTime.of(1,1,1,1,30),60);
        memory.saveSubTask(subtask1);
        Subtask subtask2 = new Subtask(4, StatusTask.NEW, "sub2", "hh", epic.getId(),LocalDateTime.of(1,1,1,2,0),30);
        memory.saveSubTask(subtask2);

         */





    }
}
