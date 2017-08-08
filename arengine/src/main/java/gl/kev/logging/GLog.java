package gl.kev.logging;

import net.gliewe.generic.function.IFuncV0;

import java.util.ArrayList;

/**
 * Created by Kevin on 18.01.2017.
 */

public class GLog {
    public static ArrayList<ILoggingHandler> LoggingHandler = new ArrayList<>();

    public static int LogFiler = 0;

    public static void doLog(String message, LogType logType, int stackOffset, Throwable exception) {
        // Check if this LogType is filtered.
        if((logType.getValue() & LogFiler) != 0)
            return;


        LogEntry entry = new LogEntry(message, logType, stackOffset + 1, exception);
        synchronized (LoggingHandler) {
            for(ILoggingHandler handler:LoggingHandler)
                try{
                    handler.handleLogEntry(entry);
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
    }

    public static void success(String message) { doLog(message, LogType.SUCCESS, 1, null); }
    public static void info(String message) { doLog(message, LogType.INFO, 1, null); }
    public static void warn(String message) { doLog(message, LogType.WARN, 1, null); }
    public static void debug(String message) { doLog(message, LogType.DEBUG, 1, null); }
    public static void exception(String message, Throwable exception) { doLog(message, LogType.EXCEPTION, 1, exception); }
    public static void error(String message, Throwable exception) { doLog(message, LogType.ERROR, 1, exception); }
    public static void fatal(String message, Throwable exception) { doLog(message, LogType.FATAL, 1, exception); }

    public static void tryNLog(String message, IFuncV0 call) {
        try {
            call.call();
        } catch (Exception ex) {
            doLog(message, LogType.EXCEPTION, 1, ex);
        }
    }
}
