package data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void testEqualsSubtask() {
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW, 7, 3);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.DONE, 9, 3);
        assertEquals(subtask1, subtask2, "subtasks are not equal");
    }
}