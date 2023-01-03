import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

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
        manager.removeTaskById(1);
        System.out.println(manager.getHistory());


    }
}
