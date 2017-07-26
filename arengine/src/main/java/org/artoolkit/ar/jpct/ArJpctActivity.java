package org.artoolkit.ar.jpct;

import com.threed.jpct.SimpleVector;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import gl.kev.ar.arengine.helper.math.Position;

/**
 * Created by portales on 11/11/15.
 */
public abstract class ArJpctActivity extends ARActivity {

    private static final String DUMMY_TEXTURE = "--dummy--";

    private ArJcptRenderer arJcptRenderer = new ArJcptRenderer(this);
    private List<TrackableObject3d> mTrackableObjects = null;

    @Override
    protected ARRenderer supplyRenderer() {
        return arJcptRenderer;
    }

    public final List<TrackableObject3d> getTrackableObject3DList() {
        mTrackableObjects = new ArrayList<TrackableObject3d>();
        populateTrackableObjects(mTrackableObjects);
        return mTrackableObjects;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove all the textures from the texture managet
        unloadTextures();
    }

    private void unloadTextures() {
        HashSet<String> names = TextureManager.getInstance().getNames();
        for (String name : names) {
            // DO NOT remove the dummy texture
            if (! name.equals(DUMMY_TEXTURE)) {
                TextureManager.getInstance().removeTexture(name);
            }
        }
    }

    public ArJcptRenderer getArJcptRenderer() {
        return arJcptRenderer;
    }

    /**
     * Override this method to provide the list of objects that are to be tracked
     * @param list
     */
    protected abstract void populateTrackableObjects(List<TrackableObject3d> list);

    /**
     * Override this method to add extra configuration to the world such as ambient light, etc
     * @param world
     */
    public abstract void configureWorld(World world);


    /**
     * Override this method to get a Callback before rendering but after Marker update.
     * @param gl
     */
    protected void beforeDraw(GL10 gl) {}

    public TrackableObject3d getTrackedObject(String name) {
        for(TrackableObject3d obj : mTrackableObjects) {
            if(obj.getName().equals(name))
                return obj;
        }
        return null;
    }

    public Position getTag(String name) {
        for(TrackableObject3d obj : mTrackableObjects) {
            if(!obj.getVisibility())
                continue;;
            for(String tagname : obj.getTags().keySet()) {
                if(tagname.equals(name)) {
                    Position pos = obj.getTags().get(name);
                    Position trackablePositinon = obj.getPosition();
                    return trackablePositinon.clone().add(pos.clone().invert());
                }
            }
        }
        return null;
    }

    /**
     * Returns the 2D Screen position of a named tracked object.
     * Returns null if it is not visible.
     * @param name
     * @return 2D position in screen.
     */
    public SimpleVector getTrackedObject2DPos(String name) {
        TrackableObject3d obj = getTrackedObject(name);
        if(obj == null)
            return null;
        if(!obj.getVisibility())
            return null;
        return project3Dto2D(obj);
    }

    public SimpleVector getTag2DPos(String name) {
        Position pos = getTag(name);
        if(pos == null)
            return null;
        return project3Dto2D(pos.getV().toSimpleVector());
    }

    /**
     * Projects a 3D Position from world space into screen space.
     * @param pos3D
     * @return 2D Screen position
     */
    public SimpleVector project3Dto2D(SimpleVector pos3D) {
        return getArJcptRenderer().project3Dto2D(pos3D);
    }

    /**
     * Projects a 3D Position from world space into screen space.
     * @param pos3D
     * @return 2D Screen position
     */
    public SimpleVector project3Dto2D(TrackableObject3d pos3D) {
        return getArJcptRenderer().project3Dto2D(pos3D.getTranslation());
    }
}
