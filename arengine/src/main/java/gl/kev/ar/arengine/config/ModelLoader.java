package gl.kev.ar.arengine.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import gl.kev.ar.arengine.AREngineActivity;
import gl.kev.ar.arengine.helper.FileSystem;
import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 08.08.17.
 */

public class ModelLoader {
    public AREngineActivity activity;
    public ModelLoader(AREngineActivity activity_) {
        activity = activity_;
    }

    public InputStream asset(String path) {
        try {
            return activity.getAssets().open(path);
        } catch (IOException e) {
            GLog.exception("Can't load model from assets: " + path, e);
            return null;
        }
    }

    public InputStream resource(String path) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        return classLoader.getResourceAsStream(path);
    }

    public InputStream storage(String path) {
        path = FileSystem.getStoregePath()+"/"+path;
        File file = new File(path);
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            GLog.exception("Can't load Model file: " + path, e);
        }
        return null;
    }
}
