package tasks;

import org.junit.jupiter.api.*;

public class TaskTest {

    @Test
    public void tasksWithEqualIdShouldBeEqual() {
        Task task1 = new Task( "Прогуляться", "После работы", Status.NEW);
        Task task2 = new Task( "Сходить в кино", "На боевик", Status.DONE);
        Assertions.assertEquals(task1, task2, "Экземпляры Task должны быть равны друг другу");
    }
}