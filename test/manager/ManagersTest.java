package manager;

import org.junit.jupiter.api.*;

public class ManagersTest {

    @Test
    void getDefaultInitializeInMemoryTaskManager() {
        Assertions.assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault());
    }

    @Test
    void getDefaultHistoryInitializeInMemoryHistoryManager() {
        Assertions.assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory());
    }
}
