package gl.kev.ar.arapp;

import android.os.Bundle;

import gl.kev.logging.GLog;
import gl.kev.logging.logger.GalgoHandler;

/**
 * Created by kevingliewe on 24.07.17.
 */

public class MainActivity_debug extends MainActivity {

    GalgoHandler galgo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galgo = new GalgoHandler(this);
        GLog.LoggingHandler.add(galgo);
        galgo.enable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        GLog.LoggingHandler.remove(galgo);
    }
}
