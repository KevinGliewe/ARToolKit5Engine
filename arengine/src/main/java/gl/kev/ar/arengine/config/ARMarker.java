package gl.kev.ar.arengine.config;

import com.threed.jpct.Object3D;

import org.artoolkit.ar.jpct.TrackableObject3d;

import java.util.HashMap;
import java.util.List;

import gl.kev.ar.arengine.AREngineActivity;
import gl.kev.ar.arengine.helper.Scripting;
import gl.kev.ar.arengine.helper.jpct.JPCTHelper;
import gl.kev.logging.GLog;

/**
 * Created by kevingliewe on 18.07.17.
 */

public class ARMarker {
    public String marker;
    public String[] script;

    public ARModel[] models;
    public boolean gizmo = false;

    public void apply(AREngineActivity activity, List<TrackableObject3d> list) {
        if(marker == null)
            return;

        GLog.debug("Loading marker " + marker);

        TrackableObject3d marker_ = new TrackableObject3d(marker);
        list.add(marker_);

        if(gizmo) {
            GLog.debug("Attach Gizmo to marker " + marker);
            marker_.addChild(JPCTHelper.createKartesianGizmo(60, 4));
        }

        if(models != null && models.length > 0) {
            for(ARModel model: models) {
                model.apply(activity, marker_, list);
            }
        } else {
            marker_.addChild(JPCTHelper.createKartesianGizmo(60,4));

        }

        marker_.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);

        if(script != null && script.length > 0){
            HashMap<String, Object> context = new HashMap<>();
            context.put("marker", marker_);
            context.put("config", this);
            Scripting.execute(script, context);
        }
    }
}
