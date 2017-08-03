package gl.kev.ar.arengine.config;

import com.threed.jpct.World;

import org.artoolkit.ar.base.NativeInterface;
import org.artoolkit.ar.jpct.TrackableObject3d;

import java.util.HashMap;
import java.util.List;

import gl.kev.ar.arengine.AREngineActivity;
import gl.kev.ar.arengine.helper.Scripting;
import gl.kev.ar.arengine.helper.math.Parsing;
import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 18.07.17.
 */

public class ARSceneConfig {
    ARMarker[] marker;
    String[] script;
    String[] script_world;
    String PatternDetectionMode = null;
    String MatrixCodeType = null;

    public void apply(AREngineActivity activity, List<TrackableObject3d> list) {
        int patternDetectionMode = getPatternDetectionMode();
        int matrixCodeType = getMatrixCodeType();

        if(patternDetectionMode != Integer.MIN_VALUE)
            NativeInterface.arwSetPatternDetectionMode(patternDetectionMode);

        if(matrixCodeType != Integer.MIN_VALUE)
            NativeInterface.arwSetMatrixCodeType(matrixCodeType);

        if(marker != null)
            for(ARMarker m: marker)
                m.apply(activity, list);


        if(script != null && script.length > 0){
            HashMap<String, Object> context = new HashMap<>();
            context.put("activity", activity);
            context.put("config", this);
            Scripting.execute(script, context);
        }
    }

    public void apply_configWorld(AREngineActivity activity, World world) {
        if(script_world != null && script_world.length > 0){
            HashMap<String, Object> context = new HashMap<>();
            context.put("activity", activity);
            context.put("config", this);
            context.put("world", world);
            Scripting.execute(script_world, context);
        }
    }

    public int getPatternDetectionMode() {
        try {
            int r = Parsing.parse(PatternDetectionMode, Integer.MIN_VALUE);
            if(r == Integer.MIN_VALUE)
                r = NativeInterface.class.getField(PatternDetectionMode).getInt(null);
            GLog.info("Set PatternDetectionMode to " + PatternDetectionMode);
            return r;
        }catch (Exception ex) {
            GLog.warn("Can't find '" + PatternDetectionMode + "' PatternDetectionMode");
            return Integer.MIN_VALUE;
        }
    }

    public int getMatrixCodeType() {
        try {
            int r = Parsing.parse(MatrixCodeType, Integer.MIN_VALUE);
            if(r == Integer.MIN_VALUE)
                r =  NativeInterface.class.getField(MatrixCodeType).getInt(null);
            GLog.info("Set MatrixCodeType to " + MatrixCodeType);
            return r;
        }catch (Exception ex) {
            GLog.warn("Can't find '" + MatrixCodeType + "' MatrixCodeType.");
            return Integer.MIN_VALUE;
        }
    }
}
