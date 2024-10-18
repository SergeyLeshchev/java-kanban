import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import manager.Managers;
import manager.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        taskManager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", Status.NEW);
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", Status.NEW, epic1.getIdOfTask());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", Status.NEW, epic1.getIdOfTask());
        taskManager.addSubtask(subtask2);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Status.NEW);
        taskManager.addEpic(epic2);

        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", Status.NEW, epic2.getIdOfTask());
        taskManager.addSubtask(subtask3);

        task1.setStatus(Status.IN_PROGRESS);
        task2.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.NEW);
        subtask3.setStatus(Status.IN_PROGRESS);

        taskManager.updateTask(task1);
        taskManager.updateTask(task2);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        System.out.println();
        System.out.println(taskManager.getTask(1));
        System.out.println(taskManager.getTask(2));
        System.out.println(taskManager.getEpic(3));
        System.out.println(taskManager.getSubtask(4));
        System.out.println(taskManager.getSubtask(5));
        System.out.println(taskManager.getEpic(6));
        System.out.println(taskManager.getSubtask(7));

        System.out.println();
        System.out.println(taskManager.getSubtasksOfEpic(taskManager.getEpic(3)));

        System.out.println("Все задачи:");
        for (Object o : taskManager.getAllTasks()) {
            System.out.println(o);
        }
        for (Object o : taskManager.getAllEpics()) {
            System.out.println(o);
        }
        for (Object o : taskManager.getAllSubtasks()) {
            System.out.println(o);
        }
        for (Subtask subtask : taskManager.getSubtasksOfEpic(epic1)) {
            System.out.println(subtask);
        }
        for (Subtask subtask : taskManager.getSubtasksOfEpic(epic2)) {
            System.out.println(subtask);
        }


        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getSubtask(5);
        taskManager.getEpic(6);
        taskManager.getSubtask(7);

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getSubtask(5);
        taskManager.getEpic(6);
        taskManager.getSubtask(7);

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        Task task3 = new Task("task3", "task3 description", Status.NEW);
        taskManager.addTask(task3);
        taskManager.getTask(8).setStatus(Status.IN_PROGRESS);
        taskManager.getTask(8);


        Epic epic3 = new Epic("Эпик 3", "Описание эпика 3", Status.NEW);
        taskManager.addEpic(epic3);
        //taskManager.getEpic(9).setStatus(Status.DONE);
        taskManager.getEpic(9);

        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4", Status.DONE, epic1.getIdOfTask());
        taskManager.addSubtask(subtask4);
        taskManager.getSubtask(10).setStatus(Status.IN_PROGRESS);
        taskManager.getSubtask(10);
        taskManager.getEpic(9);


        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}