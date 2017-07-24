package gl.kev.ar.arengine;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.threed.jpct.Config;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

import org.artoolkit.ar.jpct.ArJpctActivity;
import org.artoolkit.ar.jpct.TrackableObject3d;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import gl.kev.ar.arengine.config.ARSceneConfig;
import gl.kev.ar.arengine.helper.ActivityX;
import gl.kev.ar.arengine.helper.FileSystem;
import gl.kev.ar.arengine.helper.ViewX;
import gl.kev.ar.arengine.helper.jpct.JPCTHelper;
import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 18.07.17.
 */

public class AREngineActivity extends ArJpctActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 133;
    private static final int MY_PERMISSIONS_REQUEST_Read_Storage = 134;

    ARSceneConfig config;
    World world;

    AbsoluteLayout al_mainContainer;
    FrameLayout fl_mainARLayout;

    Map<String, View> mTrackedViews = new HashMap<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arengine);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        al_mainContainer = (AbsoluteLayout)findViewById(R.id.mainContainer);
        fl_mainARLayout = (FrameLayout)this.findViewById(R.id.mainARLayout);

        if (!checkCameraPermission()) {
            //if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) { //ASK EVERY TIME - it's essential!
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CAMERA },
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
        if(config == null) {
            config = provideConfig();
            GLog.success("Config loaded");
            GLog.debug("Config:\n" + new GsonBuilder().setPrettyPrinting().create().toJson(config));
        }
    }

    protected boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestFileReadPermission() {
        if(! (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    MY_PERMISSIONS_REQUEST_Read_Storage);
        }
    }

    @Override
    protected void populateTrackableObjects(List<TrackableObject3d> list) {
        JPCTHelper.init(this);
        Log.d(TAG, "populateTrackableObjects: ######################################################");
        if(config != null) {
            GLog.debug("Applying config to trackable objects");
            config.apply(this, list);
        }
    }

    @Override
    public void configureWorld(World world) {
        this.world = world;
        Config.farPlane = 50000;
        if(config != null)
            config.apply_configWorld(this, world);
    }

    protected ARSceneConfig provideConfig() {
        String path = getConfigPath();
        File file = new File(path);
        requestFileReadPermission();
        try {
            if(FileSystem.exists(path)) {
                GLog.debug("Load Config File " + path);
                Gson gson = new Gson();
                return gson.fromJson(new InputStreamReader(new FileInputStream(file)), ARSceneConfig.class);
            } else {
                GLog.debug("Config File " + path + " not found. Use default.");
                Gson gson = new Gson();
                return gson.fromJson(new InputStreamReader(getAssets().open("defaultconfig.json")), ARSceneConfig.class);
            }
        } catch (Exception e) {
            GLog.exception("Can't load Config file: " + path, e);
        }
        return null;
    }

    protected String getConfigPath() {
        return FileSystem.getStoregePath()+"/arapp.json";
    }

    @Override
    protected FrameLayout supplyFrameLayout() {
        return fl_mainARLayout;
    }

    public void Fullscreen() {
        ActivityX.makeFullsteen(this.getWindow());
    }

    public void addTrackedView(String name, View view) {
        mTrackedViews.put(name, view);
        ViewX.setVisible(view, false);
        al_mainContainer.addView(view);
    }

    public void removeTrackedView(String name) {
        al_mainContainer.removeView(mTrackedViews.get(name));
        mTrackedViews.remove(name);
    }

    @Override
    public void beforeDraw(GL10 gl) {

        if(mTrackedViews.size() <= 0)
            return;

        this.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   // Update Views
                   for(String name: mTrackedViews.keySet()) {
                       SimpleVector pos = getTrackedObject2DPos(name);

                       ViewX.setVisible(mTrackedViews.get(name), pos != null);

                       if(pos != null)
                           ViewX.setPosition(mTrackedViews.get(name), (int)pos.x, (int)pos.y);
                   }
               }
           }
        );
    }
}
