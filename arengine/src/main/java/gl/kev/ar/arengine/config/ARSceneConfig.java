package gl.kev.ar.arengine.config;

import com.threed.jpct.World;

import org.artoolkit.ar.base.NativeInterface;
import org.artoolkit.ar.jpct.TrackableObject3d;

import java.util.HashMap;
import java.util.List;

import gl.kev.ar.arengine.AREngineActivity;
import gl.kev.ar.arengine.helper.Scripting;
import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 18.07.17.
 */

public class ARSceneConfig {
    ARMarker[] marker;
    String[] script;
    String[] script_world;
    String PatternDetectionMode = "AR_MATRIX_CODE_DETECTION";
    String MatrixCodeType = "AR_MATRIX_CODE_3x3";

    public void apply(AREngineActivity activity, List<TrackableObject3d> list) {
        NativeInterface.arwSetPatternDetectionMode(getPatternDetectionMode());
        NativeInterface.arwSetMatrixCodeType(getMatrixCodeType());

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
            int r = NativeInterface.class.getField(PatternDetectionMode).getInt(null);
            GLog.info("Set PatternDetectionMode to " + PatternDetectionMode);
            return r;
        }catch (Exception ex) {
            GLog.warn("Can't find " + PatternDetectionMode + " PatternDetectionMode. Use AR_MATRIX_CODE_DETECTION instead");
            return NativeInterface.AR_MATRIX_CODE_DETECTION;
        }
    }

    public int getMatrixCodeType() {
        try {
            int r =  NativeInterface.class.getField(MatrixCodeType).getInt(null);
            GLog.info("Set MatrixCodeType to " + MatrixCodeType);
            return r;
        }catch (Exception ex) {
            GLog.warn("Can't find " + MatrixCodeType + " MatrixCodeType. Use AR_MATRIX_CODE_3x3 instead");
            return NativeInterface.AR_MATRIX_CODE_3x3;
        }
    }
}
