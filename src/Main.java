import data.Epic;
import data.Status;
import data.Subtask;
import data.Task;
import manager.Managers;
import manager.TaskManager;

public class Main {
    public static void main(String[] args) {
        // Пользовательский сценарий:
        TaskManager taskManager = Managers.getDefaultTaskManager();

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

        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", Status.NEW, epic1.getIdOfTask());
        taskManager.addSubtask(subtask3);

        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", Status.NEW);
        taskManager.addEpic(epic2);

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getSubtask(5);
        taskManager.getSubtask(6);
        taskManager.getEpic(7);

        // Вызовем некоторые задачи для создания повторов
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubtask(4);
        taskManager.getEpic(7);

        // Задачи должны быть распечатаны в порядке id: 1, 5, 6, 2, 3, 4, 7
        System.out.println("История: Задачи должны быть распечатаны в порядке id: 1, 5, 6, 2, 3, 4, 7");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }

        // Удалим одну задачу и эпик с тремя подзадачами. Выведем что получилось:
        taskManager.removeTask(1);
        taskManager.removeEpic(3);
        System.out.println("История после удаления: Задачи должны быть распечатаны в порядке id: 2, 7");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
