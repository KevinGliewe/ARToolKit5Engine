package gl.kev.logging.logger;

import android.util.Log;

import gl.kev.logging.ILoggingHandler;
import gl.kev.logging.LogEntry;


/**
 * Created by Kevin on 18.01.2017.
 */

public class AndroidHandler implements ILoggingHandler {
    @Override
    public void handleLogEntry(LogEntry logEntry) {
        //StackTraceElement cause = logEntry.stackTrace[logEntry.stackTrace.length - 1];
        StackTraceElement cause = logEntry.stackTrace[2];
        String tag = cause.getClassName() + "." + cause.getMethodName();

        switch (logEntry.logType) {
            case DEBUG:
                Log.d(tag, logEntry.message, logEntry.exception);
                break;
            case SUCCESS:
                Log.i(tag, logEntry.message, logEntry.exception);
                break;
            case INFO:
                Log.i(tag, logEntry.message, logEntry.exception);
                break;
            case WARN:
                Log.w(tag, logEntry.message, logEntry.exception);
                break;
            case EXCEPTION:
                Log.e(tag, logEntry.message, logEntry.exception);
                break;
            case ERROR:
                Log.e(tag, logEntry.message, logEntry.exception);
                break;
            case FATAL:
                Log.e(tag, logEntry.message, logEntry.exception);
                break;

            default:
                Log.wtf(tag, logEntry.message, logEntry.exception);
        }
    }

    @Override
    public void shutdown() {

    }
}
