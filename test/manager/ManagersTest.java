package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultTaskManagerTest() {
        assertNotNull(Managers.getDefaultTaskManager(), "getDefaultTaskManager() not works correctly");
    }

    @Test
    void getDefaultHistoryManagerTest() {
        assertNotNull(Managers.getDefaultHistoryManager(), "getDefaultHistoryManager() not works correctly");
    }
}