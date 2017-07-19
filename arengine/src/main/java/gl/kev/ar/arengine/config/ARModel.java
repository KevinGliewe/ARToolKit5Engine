package gl.kev.ar.arengine.config;

import android.os.Environment;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

import org.artoolkit.ar.jpct.TrackableObject3d;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import gl.kev.ar.arengine.AREngineActivity;
import gl.kev.ar.arengine.helper.FileSystem;
import gl.kev.ar.arengine.helper.Scripting;
import gl.kev.ar.arengine.helper.jpct.Node3D;
import gl.kev.ar.arengine.helper.math.Position;
import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 18.07.17.
 */

public class ARModel {
    public String model;
    public String[] script;

    public double scale = 1.0;

    public double x = 0;
    public double y = 0;
    public double z = 0;

    public double rx = 0;
    public double ry = 0;
    public double rz = 0;

    public int transparency = 20;

    public void apply(AREngineActivity activity, TrackableObject3d marker, List<TrackableObject3d> list) {
        HashMap<String, Object> context = new HashMap<>();
        context.put("loader", new ModelLoader(activity));
        context.put("activity", activity);


        InputStream inputStream = null;

        if(model != null) {
            inputStream = (InputStream) Scripting.execute(model, context);
        } else {
            GLog.info("model is null");
        }

        Object3D[] object3DfArr = null;

        if(inputStream != null) {
            object3DfArr = Loader.loadOBJ(inputStream, null, (float)scale);
            Node3D node = new Node3D();
            for(Object3D object3Df : object3DfArr) {
                node.addChild(object3Df);
            }

            node.setTransparency(transparency) ;
            node.setTransparencyMode(Object3D.TRANSPARENCY_MODE_DEFAULT);

            node.rotateX((float)rx);
            node.rotateY((float)ry);
            node.rotateZ((float)rz);

            node.translate((float)x,(float)y,(float)z);
            node.scale((float)scale);
            node.setOrigin(new SimpleVector(0, 0, 0));
            node.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);

            marker.addChild(node);
        } else {
            GLog.info("'" + model + "' returned null");
        }

        if(script != null && script.length > 0){
            context = new HashMap<>();
            context.put("marker", marker);
            context.put("config", this);
            context.put("objects3D", object3DfArr);
            Scripting.execute(script, context);
        }
    }

    public class ModelLoader {
        public AREngineActivity activity;
        public ModelLoader(AREngineActivity activity) {
            this.activity = activity;
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
}
