package gl.kev.logging.logger;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.inaka.galgo.Galgo;
import com.inaka.galgo.GalgoOptions;

import gl.kev.logging.ILoggingHandler;
import gl.kev.logging.LogEntry;


public class GalgoHandler implements ILoggingHandler {
    private Activity context;

    public GalgoHandler(Activity context) {
        this.context = context;
    }

    public void enable() {
        GalgoOptions options = new GalgoOptions.Builder()
                .numberOfLines(15)
                .backgroundColor(Color.parseColor("#D9d6d6d6"))
                .textColor(Color.BLACK)
                .textSize(15)
                .build();
        Galgo.enable(this.context, options);
    }

    public void disable() {
        Galgo.disable(context);
    }

    @Override
    public void handleLogEntry(LogEntry logEntry) {
        if(logEntry.exception == null)
            Galgo.log(logEntry.message);
        else
            Galgo.log(logEntry.message + " (" + logEntry.exception.getMessage() + ")");
    }

    @Override
    public void shutdown() {
        Galgo.disable(context);
    }
}
