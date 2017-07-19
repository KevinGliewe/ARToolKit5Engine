package gl.kev.logging;

/**
 * Created by Kevin on 18.01.2017.
 */

public interface ILoggingHandler {
    void handleLogEntry(LogEntry logEntry);
    void shutdown();
}
