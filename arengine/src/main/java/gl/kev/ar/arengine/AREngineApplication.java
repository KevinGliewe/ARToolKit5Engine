package gl.kev.ar.arengine;

import android.app.Application;
import android.util.Log;

import org.artoolkit.ar.base.assets.AssetHelper;

import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 18.07.17.
 */

public class AREngineApplication extends Application {
    public static String TAG = "AREngineApplication";
    private static AREngineApplication sInstance;

    // Anywhere in the application where an instance is required, this method
    // can be used to retrieve it.
    public static AREngineApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "##############################################");
        Log.i(TAG, "onCreate: Initializing Application");
        Log.i(TAG, "##############################################");


        super.onCreate();
        sInstance = this;
        ((AREngineApplication) sInstance).initializeInstance();

        GLog.LoggingHandler.add(new gl.kev.logging.logger.AndroidHandler());
    }

    // Here we do one-off initialisation which should apply to all activities
    // in the application.
    protected void initializeInstance() {

        // Unpack assets to cache directory so native library can read them.
        // N.B.: If contents of assets folder changes, be sure to increment the
        // versionCode integer in the AndroidManifest.xml file.
        AssetHelper assetHelper = new AssetHelper(getAssets());
        assetHelper.cacheAssetFolder(getInstance(), "Data");
    }
}
