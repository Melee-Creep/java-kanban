package tasks;

import org.junit.jupiter.api.*;

public class SubtaskTest {

    @Test
    public void SubtasksWithEqualIdShouldBeEqual() {
        Subtask subtask1 = new Subtask("Прогуляться","После работы",1);
        Subtask subtask2 = new Subtask("Сходить в кино","На боевик",1);
        Assertions.assertEquals(subtask1, subtask2,"Наследники класса Task должны быть равны друг другу");
    }
}
