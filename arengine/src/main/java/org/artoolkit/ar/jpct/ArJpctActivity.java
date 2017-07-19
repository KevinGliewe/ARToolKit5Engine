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

/**
 * Created by portales on 11/11/15.
 */
public abstract class ArJpctActivity extends ARActivity {

    private static final String DUMMY_TEXTURE = "--dummy--";

    private ArJcptRenderer arJcptRenderer = new ArJcptRenderer(this);

    @Override
    protected ARRenderer supplyRenderer() {
        return arJcptRenderer;
    }

    public final List<TrackableObject3d> getTrackableObject3DList() {
        List<TrackableObject3d> list = new ArrayList<TrackableObject3d>();
        populateTrackableObjects(list);
        return list;
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

    /**
     * Projects a 3D Position from world space into screen space.
     * @param pos3D
     * @return 2D Screen position
     */
    public SimpleVector project3Dto2D(SimpleVector pos3D) {
        return getArJcptRenderer().project3Dto2D(pos3D);
    }

}
