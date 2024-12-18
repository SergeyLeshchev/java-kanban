package data;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void testEqualsTask() {
        Task task1 = new Task("task1", "task1 description", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10), 5);
        Task task2 = new Task("task2", "task2 description", Status.DONE,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10), 5);
        assertEquals(task1, task2, "tasks are not equal");
    }
}
