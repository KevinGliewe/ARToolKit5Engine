package gl.kev.logging;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 18.01.2017.
 */

public enum LogType {
    SUCCESS(1), INFO(2), WARN(4), DEBUG(8), EXCEPTION(16), ERROR(32), FATAL(64);


    private final Integer value;

    LogType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    private static Map<Integer, LogType> map = new HashMap<Integer, LogType>();

    static {
        for (LogType legEnum : LogType.values()) {
            map.put(legEnum.value, legEnum);
        }
    }

    public static LogType valueOf(int value) {
        return map.get(value);
    }

    public static String getFaultTypeName(LogType faultType) {
        switch (faultType) {
            case SUCCESS:
                return "SUCCESS";
            case INFO:
                return "INFO";
            case WARN:
                return "Materialfehler";
            case DEBUG:
                return "DEBUG";
            case EXCEPTION:
                return "EXCEPTION";
            case ERROR:
                return "Hoher Drehmoment";
            case FATAL:
                return "FATAL";
        }

        return "???";
    }
}
