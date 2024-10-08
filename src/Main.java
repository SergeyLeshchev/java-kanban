public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task();
        task1.setTitle("Задача 1");
        task1.setDescription("Описание задачи 1");
        task1.setStatus(Status.NEW);
        taskManager.addTask(task1);

        Task task2 = new Task();
        task2.setTitle("Задача 2");
        task2.setDescription("Описание задачи 2");
        task2.setStatus(Status.NEW);
        taskManager.addTask(task2);

        Epic epic1 = new Epic();
        epic1.setTitle("Эпик 1");
        epic1.setDescription("Описание эпика 1");
        epic1.setStatus(Status.NEW);
        taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask();
        subtask1.setTitle("Подзадача 1");
        subtask1.setDescription("Описание подзадачи 1");
        subtask1.setStatus(Status.NEW);
        subtask1.setEpicId(epic1.getIdOfTask());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask();
        subtask2.setTitle("Подзадача 2");
        subtask2.setDescription("Описание подзадачи 2");
        subtask2.setStatus(Status.NEW);
        subtask2.setEpicId(epic1.getIdOfTask());
        taskManager.addSubtask(subtask2);

        Epic epic2 = new Epic();
        epic2.setTitle("Эпик 2");
        epic2.setDescription("Описание эпика 2");
        epic2.setStatus(Status.NEW);
        taskManager.addEpic(epic2);

        Subtask subtask3 = new Subtask();
        subtask3.setTitle("Подзадача 3");
        subtask3.setDescription("Описание подзадачи 3");
        subtask3.setStatus(Status.NEW);
        subtask3.setEpicId(epic2.getIdOfTask());
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
        System.out.println(taskManager.getTaskByID(1));
        System.out.println(taskManager.getTaskByID(2));
        System.out.println(taskManager.getEpicByID(3));
        System.out.println(taskManager.getSubtaskByID(4));
        System.out.println(taskManager.getSubtaskByID(5));
        System.out.println(taskManager.getEpicByID(6));
        System.out.println(taskManager.getSubtaskByID(7));

        System.out.println();
        System.out.println(taskManager.subtasksOfEpic(taskManager.getEpicByID(3)));

        taskManager.removeTask(2);
        taskManager.removeEpic(3);
        taskManager.removeSubtask(7);

        System.out.println(taskManager.getAllTasks());

        System.out.println(taskManager.getEpicByID(6));
    }
}