package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskPlannerTest {

    TaskPlanner taskPlanner;

    @BeforeEach
    void init() {
        taskPlanner = new TaskPlanner();
    }

    @Test
    void shouldIntervalTimeSlots() {
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 0, 0);

        Map<LocalDateTime, Boolean> interval = taskPlanner.IntervalTimeMap();

        assertEquals(35136, interval.size(), "Количество ячеек не соответствует году");
        assertEquals(false, interval.get(time), "Ячейка отдает не верное булево значение");
    }

    @Test
    void shouldUpdateTimePlus() {
        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 1, 9, 59);
        LocalDateTime time2 = LocalDateTime.of(2021, 1, 1, 1, 15);

        LocalDateTime timeCheck = taskPlanner.UpdateInterval(time);

        assertEquals(time2, timeCheck, "Время не совпадает");
    }

    @Test
    void shouldUpdateTimeMinus() {
        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 1, 5, 59);
        LocalDateTime time2 = LocalDateTime.of(2021, 1, 1, 1, 0);

        LocalDateTime timeCheck = taskPlanner.UpdateInterval(time);

        assertEquals(time2, timeCheck, "Время не совпадает");
    }

    @Test
    void shouldRoundUpTime() {
        LocalDateTime time = LocalDateTime.of(2021, 1, 1, 1, 0, 59);
        LocalDateTime time2 = LocalDateTime.of(2021, 1, 1, 1, 0);

        LocalDateTime timeCheck = taskPlanner.UpdateInterval(time);

        assertEquals(time2, timeCheck, "Время не совпадает");
    }
}