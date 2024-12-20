package data;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    @Test
    void testEqualsEpic() {
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10),
                LocalDateTime.parse("2003-12-21T21:21:21"), 7);
        Epic epic2 = new Epic("epic2", "epic2 description", Status.DONE,
                LocalDateTime.parse("2003-12-21T21:21:21"), Duration.ofMinutes(10),
                LocalDateTime.parse("2003-12-21T21:21:21"), 7);
        assertEquals(epic1, epic2, "epics are not equal");
    }

    @Test
    void addSubtaskIdTest() {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        epic.addSubtaskId(1);
        assertEquals(1, epic.getSubtasksIds().size(), "idOfSubtask not added");
    }

    @Test
    void removeSubtaskIdTest() {
        Epic epic = new Epic("epic", "epic description", Status.NEW);
        epic.addSubtaskId(1);
        epic.removeSubtaskId(1);
        assertEquals(0, epic.getSubtasksIds().size(), "idOfSubtask not removed");
    }
}
