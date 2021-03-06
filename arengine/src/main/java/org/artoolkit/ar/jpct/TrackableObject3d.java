package org.artoolkit.ar.jpct;

import android.content.Context;
import android.util.Log;

import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.World;

import org.artoolkit.ar.base.ARToolKit;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gl.kev.ar.arengine.config.ARMarkerOptions;
import gl.kev.ar.arengine.helper.jpct.Node3D;
import gl.kev.ar.arengine.helper.math.Position;
import gl.kev.logging.GLog;


/**
 * Created by portales on 12/11/15.
 *
 * 3D Object that is anchored to a marker
 *
 */
public class TrackableObject3d extends Object3D {
    public final static String TAG = "TrackableObject3d";

    private final String mMarkerString;

    public int getmMarkerId() {
        return mMarkerId;
    }

    private int mMarkerId;
    private Matrix projMatrix = new Matrix();
    private List<Object3D> mChildren = new ArrayList<Object3D>();
    private List<TrackableLight> mLights = new ArrayList<TrackableLight>();
    private boolean mPreviousVisibility;
    private OnVisibilityChangeListener mVisibilityChangeListener;

    private Position mPosition = new Position();

    private String name = "NONAMME";

    private Map<String, Position> mTags = new HashMap<>();

    public void setOptions(ARMarkerOptions options) {
        this.options = options;
    }

    private ARMarkerOptions options = null;

    private int lerped = Integer.MAX_VALUE;
    private int frames_notVisible = 99999;

    public boolean lerping = false;

    public TrackableObject3d(String markerString) {
        super(2); // 2 mx triangles, this object is the parent of all the trackable items
        mMarkerString = markerString;
        mVisibilityChangeListener = null;
        mPreviousVisibility = false;
    }

    public TrackableObject3d(String markerString, Object3D child) {
        this(markerString);
        addChild(child);
    }

    public void addOnVisibilityChangeListener(OnVisibilityChangeListener listener) {
        mVisibilityChangeListener = listener;
    }

    /**
     * Loads a model on .3ds format and adds it as a child of the trackable object
     *
     * @param c A context, used to access the assets directory
     * @param path The path to the .3ds inside the assets directory (i.e. model.3ds)
     * @param scale The scale to be applied when loading the model
     */
    public void add3DSModel(Context c, String path, float scale) {
        try {
            Object3D [] object3D = Loader.load3DS(c.getAssets().open(path), scale);
            for (int i=0; i<object3D.length; i++) {
                addChild(object3D[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal call used by ArJpctRenderer that registers the marker into ARToolKit
     * It is called automatically during the initialization of the AR Scene
     * @return
     */
    public boolean registerMarker() {
        mMarkerId = ARToolKit.getInstance().addMarker(mMarkerString);
        if(options != null)
            options.apply(mMarkerId);
        return mMarkerId != -1;
    }

    /**
     * Updates the position and rotation of the object to the one on the marker
     * This is called automatically by ArJpctRenderer before rendering each frame
     */
    public void updateMarkerTransformation() {
        // Update the position and rotation of the trackable object
        boolean markerVisible = ARToolKit.getInstance().queryMarkerVisible(mMarkerId);
        setVisibility(markerVisible);
        if (markerVisible != mPreviousVisibility && mVisibilityChangeListener != null) {
            mVisibilityChangeListener.onVisibilityChanged(markerVisible);
        }
        mPreviousVisibility = markerVisible;
        if (markerVisible) {
            frames_notVisible=0;
            float[] transformation = ARToolKit.getInstance().queryMarkerTransformation(mMarkerId);
            Position pos = new Position(new Matrix4f(transformation));

            if(lerping) {
                Position diff = mPosition.getDiff(pos, null);
                //GLog.debug("~~~~~~~~~~~~~~~ diff: " + diff.toString());
                float diffangle = diff.getQ().x + diff.getQ().y + diff.getQ().z;

                if (diffangle < 0.0)
                    diffangle = -diffangle;

                //GLog.debug("~~~~~~~~~~~~~ diff: " + diffangle);

                if (diffangle > 0.06 && lerped < 10) {
                    GLog.info("lerping... " + lerped);
                    Position lerped = new Position(
                            mPosition.getV(),
                            mPosition.getQ().nlerp(pos.getQ(), 0.1f, new Quaternionf())
                    );
                    pos = lerped;
                    transformation = lerped.toMatrix4f(null).get(transformation);
                    this.lerped++;
                } else {
                    lerped = 0;
                }
            }

            projMatrix.setDump(transformation);
            clearTranslation();
            translate(projMatrix.getTranslation());
            setRotationMatrix(projMatrix);

            mPosition = pos;

            // Also, update all the lights
            for (int i=0; i<mLights.size(); i++) {
                // Lights do not rotate
                TrackableLight l = mLights.get(i);
                l.update(projMatrix.getTranslation());
                mLights.get(i).setVisibility(true);
            }
        } else {
            frames_notVisible++;
            if(frames_notVisible > 3)
                this.lerped = Integer.MAX_VALUE;
        }
    }

    @Override
    public void setVisibility (boolean visible) {
        super.setVisibility(visible);
        for (int i=0; i<mChildren.size(); i++) {
            mChildren.get(i).setVisibility(visible);
        }
        for (int i=0; i<mLights.size(); i++) {
            mLights.get(i).setVisibility(visible);
        }
    }

    @Override
    public void addChild(Object3D object3D) {
        super.addChild(object3D);
        // Keep it in a local list
        mChildren.add(object3D);
    }

    @Override
    public void removeChild(Object3D object3D) {
        super.removeChild(object3D);
        // remove it from the local list
        mChildren.remove(object3D);
    }

    public List<Object3D> getChildren() {
        return mChildren;
    }

    /**
     * Add the object to the world, including all the children
     * @param world
     */
    public void addToWorld(World world) {
        Log.d(TAG, "addToWorld: " + mMarkerString);

        world.addObject(this);
        for (int i=0; i<mChildren.size(); i++) {
            Object3D o = mChildren.get(i);
            if(o instanceof Node3D)
                ((Node3D)o).addToWorld(world);
            else
                world.addObject(o);
        }
        for (int i=0; i<mLights.size(); i++) {
            mLights.get(i).addToWorld(world);
        }
    }

    public Position getPosition() {
        return mPosition;
    }

    public void setPosition(Position newPos) {
        if(newPos == mPosition)
            return;

        mPosition = newPos;

        float[] transformation = mPosition.toMatrix4f(null).toArray(null);
        projMatrix.setDump(transformation);
        clearTranslation();
        translate(projMatrix.getTranslation());
        setRotationMatrix(projMatrix);
    }

    public void addLight(TrackableLight light) {
        mLights.add(light);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Position> getTags() { return mTags; }
}
