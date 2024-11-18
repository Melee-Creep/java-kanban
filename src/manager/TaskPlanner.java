package manager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class TaskPlanner {

    private static final int INTERVAL_15_MINUTE = 15;
    private static final LocalDateTime START = LocalDateTime.of(2024,1,1,0,0);
    private static final LocalDateTime END = START.plusYears(1);

    public Map<LocalDateTime, Boolean> intervalTimeMap() {
        Map<LocalDateTime, Boolean> interval = new HashMap<>();
        LocalDateTime current = START;
        while (current.isBefore(END)) {
            interval.put(current, false);
            current = current.plusMinutes(INTERVAL_15_MINUTE);
        }
        return interval;
    }

    public LocalDateTime updateInterval(LocalDateTime time) {
        LocalDateTime timeWithoutSeconds = time.truncatedTo(ChronoUnit.MINUTES);
        int remainder = timeWithoutSeconds.getMinute() % 15;
        if (remainder >= 7) {
            return timeWithoutSeconds.plusMinutes(15 - remainder);
        }
        return timeWithoutSeconds.minusMinutes(remainder);
    }
}