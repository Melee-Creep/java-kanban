package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import converter.LocalDateTimeConverter;

import java.time.LocalDateTime;

public class Managers {
    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
                .create();
    }
}