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
import java.util.Map;

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

    private static Map<String, Object3D[]> modelcache = new HashMap<>();

    public static void preloadModel(AREngineActivity activity, ARModel m) {
        Object3D[] object3DfArr = null;
        InputStream inputStream = null;

        if(m.model != null) {
            if(!modelcache.containsKey(m.model)) {
                HashMap<String, Object> context = new HashMap<>();
                context.put("loader", new ModelLoader(activity));
                context.put("activity", activity);
                inputStream = (InputStream) Scripting.execute(m.model, context);
                object3DfArr = Loader.loadOBJ(inputStream, null, 1.0F);
                modelcache.put(m.model, object3DfArr);
            }
        }
    }

    public void apply(AREngineActivity activity, TrackableObject3d marker, List<TrackableObject3d> list) {
        HashMap<String, Object> context = new HashMap<>();
        context.put("loader", new ModelLoader(activity));
        context.put("activity", activity);

        Object3D[] object3DfArr = null;
        InputStream inputStream = null;

        if(model != null) {
            if(modelcache.containsKey(model))
                object3DfArr = modelcache.get(model);
            else
                inputStream = (InputStream) Scripting.execute(model, context);
        } else {
            GLog.info("model is null");
        }

        if(inputStream != null || object3DfArr != null) {
            if(inputStream != null)
                object3DfArr = Loader.loadOBJ(inputStream, null, 1.0F);
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

            modelcache.put(model, object3DfArr);
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
}
