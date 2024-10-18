package data;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void testEqualsEpic() {
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW, 7);
        Epic epic2 = new Epic("epic2", "epic2 description", Status.NEW, 7);
        assertEquals(epic1, epic2, "epics are not equal");
    }
}