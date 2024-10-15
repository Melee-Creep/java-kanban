package tasks;

import org.junit.jupiter.api.*;

public class EpicTest {

    @Test
    public void EpicEqualBeEqual() {
        Epic epic1 = new Epic("Развлечения","В этом месяце", Status.NEW);
        Epic epic2 = new Epic("Развлечения","В этом месяце", Status.NEW);
        Assertions.assertEquals(epic1, epic1, "Наследники класса Task должны быть равны друг другу");
    }
}
