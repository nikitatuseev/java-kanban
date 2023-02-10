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
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
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

        Task task = new Task("dd", "ss", LocalDateTime.now(), 30);
        manager.saveTask(task);

        Epic epic = new Epic("sd", "wd");
        manager.saveEpic(epic);
        Subtask subTask = new Subtask("gd", "wdw", epic.getId(), LocalDateTime.now().plusMinutes(30), 60);
        manager.saveSubTask(subTask);

        Subtask subtask2 = new Subtask("grfg", "efg", epic.getId(), LocalDateTime.now().plusMinutes(90), 60);
        manager.saveSubTask(subtask2);
        Subtask subtask3 = new Subtask("okd", "jsidj", epic.getId(), LocalDateTime.now().plusMinutes(150), 20);
        manager.saveSubTask(subtask3);

        manager.removeSubTaskById(3);
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubTask());
        System.out.println("LKKK");
        manager.removeAllTask();
        System.out.println(manager.getSortedTasks());

        //memory.getEpicById(2);
/*
        Task newTaskFirst = new Task("taskH", "description");
        newTaskFirst.setId(1);
        newTaskFirst.setStatus(StatusTask.DONE);
        //memory.updateTask(newTaskFirst);

/*
        System.out.println("печать всех обычных задач");
        Task taskFirst = new Task("task1", "description");
        manager.saveTask(taskFirst);
        Task taskSecond = new Task("task2", "hjh");
        manager.saveTask(taskSecond);

        System.out.println(manager.getAllTask());

        System.out.println();

        Epic firstEpic = new Epic("epic1", "описание");
        manager.saveEpic(firstEpic);

        Epic secondEpic = new Epic("epic2", "описание");
        manager.saveEpic(secondEpic);

        System.out.println("печать всех subTask");
        Subtask firstSubTask = new Subtask("sub1", "description", firstEpic.getId());
        manager.saveSubTask(firstSubTask);

        Subtask secondSubTask = new Subtask("sub2", "hh", firstEpic.getId());
        manager.saveSubTask(secondSubTask);

        Subtask thirdSubTask = new Subtask("sub3", "описание", secondEpic.getId());
        manager.saveSubTask(thirdSubTask);
        System.out.println(manager.getAllSubTask());
        System.out.println();

        System.out.println("Обновление");
        Task newTaskFirst = new Task("task1", "description");
        newTaskFirst.setId(1);
        newTaskFirst.setStatus(StatusTask.DONE);
        manager.updateTask(newTaskFirst);

        System.out.println(manager.getAllTask());

        Subtask newThirdSubTask = new Subtask("sub3", "jhjh", secondEpic.getId());
        newThirdSubTask.setStatus(StatusTask.DONE);
        newThirdSubTask.setId(7);
        manager.updateSubTask(newThirdSubTask);


        Subtask newSubTaskFirst = new Subtask("sub1", "description", firstEpic.getId());
        newSubTaskFirst.setStatus(StatusTask.DONE);
        newSubTaskFirst.setId(5);
        manager.updateSubTask(newSubTaskFirst);
        System.out.println();

        System.out.println(manager.getAllSubTask());
        System.out.println();

        Epic newEpic = new Epic("newEpic1", "newDescription");
        newEpic.setId(3);
        manager.updateEpic(newEpic);


        System.out.println("печать всех эпиков");
        System.out.println(manager.getAllEpic());
        System.out.println();

        System.out.println("Все сабы по id эпика");
        System.out.println(manager.getSubtasksByNameEpic(3));
        System.out.println();

        System.out.println(manager.getAllEpic());
        System.out.println();

        System.out.println("Проверяю работу истории просмотра");
        manager.getEpicById(3);
        manager.getSubTaskById(5);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(4);
        System.out.println(manager.getHistory());
        manager.getEpicById(3);
        manager.removeSubTaskById(5);
        manager.removeEpicById(4);
        //manager.removeTaskById(1);
        System.out.println(manager.getHistory());
        //удалить все что ниже

 */
    }
}
