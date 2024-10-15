package tasks;

import org.junit.jupiter.api.*;

public class TaskTest {

    @Test
    public void tasksWithEqualIdShouldBeEqual() {
        Task task1 = new Task( "Прогуляться", "После работы", Status.NEW);
        Task task2 = new Task( "Прогуляться", "После работы", Status.NEW);
        Assertions.assertEquals(task1, task2);
    }
}