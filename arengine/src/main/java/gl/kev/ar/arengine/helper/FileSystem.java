package gl.kev.ar.arengine.helper;

import android.os.Environment;

import java.io.File;

/**
 * Created by kevingliewe on 19.07.17.
 */

public class FileSystem {
    private static final String[] storage_paths = new String[] {
            "/sdcard",
            "/storage/emulated/0"
    };

    public static String getStoregePath() {
        for(String path: storage_paths)
            if(exists(path))
                return path;
        return Environment.getExternalStorageState();
    }

    public static boolean exists(String path) {
        return new File(path).exists();
    }
}
