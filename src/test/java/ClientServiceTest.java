import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    @Test
    void testMainClassesCompilation() {
        // Просто проверяем, что классы компилируются и загружаются
        assertDoesNotThrow(() -> {
            Class.forName("ClientService");
            Class.forName("FirstClient");
            Class.forName("SecondClient");
            Class.forName("Server");
            Class.forName("ServerService");
            Class.forName("Logger");
        });
    }

    @Test
    void testSettingsFileIntegration() throws IOException {
        // Создаем тестовый settings.txt
        String settingsContent = "8080\nlocalhost";
        Files.write(Path.of("settings.txt"), settingsContent.getBytes());

        // Проверяем, что файл читается корректно
        assertTrue(Files.exists(Path.of("settings.txt")));

        String content = Files.readString(Path.of("settings.txt"));
        String[] lines = content.split("\n");
        assertEquals(2, lines.length);
        assertEquals("8080", lines[0].trim());
        assertEquals("localhost", lines[1].trim());

        Files.deleteIfExists(Path.of("settings.txt"));
    }

    @Test
    void testLoggerIntegration() throws IOException {
        Logger logger = Logger.getInstance();

        // Тестируем функциональность логгера
        String testMessage = "Test message from ClientServiceTest";
        logger.log(testMessage);

        assertTrue(Files.exists(Path.of("file.log")));
        String logContent = Files.readString(Path.of("file.log"));
        assertTrue(logContent.contains(testMessage));

        Files.deleteIfExists(Path.of("file.log"));
    }
}