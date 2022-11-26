import managers.Manager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
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
        Task newTaskFirst = new Task("task1", "description", "DONE", 1);
        manager.updateTask(newTaskFirst);

        System.out.println(manager.getAllTask());

        Subtask newSubTaskFirst = new Subtask("sub1", "description", 3, "DONE", 5);
        manager.updateSubTask(newSubTaskFirst);
        System.out.println();

        System.out.println(manager.getAllSubTask());
        System.out.println();

        Epic newEpic = new Epic("newEpic1", "newDescription", 3);
        manager.updateEpic(newEpic);

        System.out.println("печать всех эпиков");
        System.out.println(manager.getAllEpic());
        System.out.println();

        System.out.println("Все сабы по id эпика");
        manager.getAllSubByNameEpic(3);


    }
}
