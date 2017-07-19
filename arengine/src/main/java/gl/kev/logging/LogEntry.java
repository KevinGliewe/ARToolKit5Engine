package gl.kev.logging;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Kevin on 18.01.2017.
 */

public class LogEntry {

    public String message;
    public LogType logType;
    public Date timeStamp;
    public StackTraceElement[] stackTrace;
    public Exception exception;
    public Thread thread;

    public LogEntry(String message, LogType logType, int stackOffset, Exception exception) {
        this.message = message;
        this.logType = logType;
        this.timeStamp = new Date();
        this.exception = exception;
        this.thread = Thread.currentThread();

        StackTraceElement[] cause = Thread.currentThread().getStackTrace();
        this.stackTrace = Arrays.copyOfRange(cause, 1 + stackOffset, cause.length);
    }
}
