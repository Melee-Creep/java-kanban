package tasks;

import org.junit.jupiter.api.*;

public class EpicTest {

    @Test
    public void EpicEqualBeEqual() {
        Epic epic1 = new Epic("Развлечения","В этом месяце", Status.NEW);
        Epic epic2 = new Epic("Учеба","Зачёт в сентябре", Status.IN_PROGRESS);
        Assertions.assertEquals(epic1, epic2, "Наследники класса Task должны быть равны друг другу");
    }
}
