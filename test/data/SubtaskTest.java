package data;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {
    @Test
    void testEqualsSubtask() {
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10), 7, 3);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.DONE,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10), 9, 3);
        assertEquals(subtask1, subtask2, "subtasks are not equal");
    }
}
