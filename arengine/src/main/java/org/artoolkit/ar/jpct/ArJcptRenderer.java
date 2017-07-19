package org.artoolkit.ar.jpct;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

import org.artoolkit.ar.base.ARToolKit;
import org.artoolkit.ar.base.rendering.ARRenderer;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by portales on 11/11/15.
 */
public class ArJcptRenderer extends ARRenderer {

    private final ArJpctActivity mActivity;
    private List<TrackableObject3d> mTrackableObjects;

    protected World mWorld;
    protected Camera mCamera;
    protected FrameBuffer mBuffer;
    private Matrix projMatrix = new Matrix();
    private boolean mFovSet;



    public Camera getCamera() {
        return mCamera;
    }

    public FrameBuffer getBuffer() {
        return mBuffer;
    }

    public Matrix getProjMatrix() {
        return projMatrix;
    }


    public ArJcptRenderer(ArJpctActivity arJpctActivity) {
        mActivity = arJpctActivity;
    }

    /**
     * Configuration of the AR Scene
     * Initializes jPCT and gets the trackable objects list
     * Registers the markers and adds the objects to the world
     */
    @Override
    public boolean configureARScene() {
        // Initialize the game world and the camera
        mWorld = new World();
        mCamera = mWorld.getCamera();
        // Set the FOV based on the camera parameters
        // This FOW is correct when using
        android.hardware.Camera.Parameters params = mActivity.getCameraPreview().getCameraParameters();
        // Setting the FOV based on the camera params, this seems to work fine with 640x480
        float fov = params.getHorizontalViewAngle();
        float yfov = params.getVerticalViewAngle();
        mCamera.setFOV(mCamera.convertDEGAngleIntoFOV(fov));
        mCamera.setYFOV(mCamera.convertDEGAngleIntoFOV(yfov));

        mActivity.configureWorld(mWorld);

        // Get the activity list of trackable objects
        mTrackableObjects = mActivity.getTrackableObject3DList();

        // Load all the markers and add the objects to the world
        for (int i=0; i<mTrackableObjects.size(); i++) {
            TrackableObject3d trackableObject = mTrackableObjects.get(i);
            // Load the marker
            if (! trackableObject.registerMarker() ) {
                // If there was a problem, return false
                return false;
            }
            // Add the object to the world, note that mWorld.addObject is not recursive
            trackableObject.addToWorld(mWorld);
        }

        mWorld.buildAllObjects();

        mFovSet = false;

        return true;
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int w, int h) {
        super.onSurfaceChanged(unused, w, h);
        mBuffer = new FrameBuffer(unused,w,h);
    }

    /**
     * Override the draw function from ARRenderer
     * This one sets the correct position and orientation from the camera
     * For each trackable object, checks if the marker is visible and if so, updates the object
     */
    @Override
    public final void draw(GL10 gl) {
        mBuffer.clear();

        float[] projection = ARToolKit.getInstance().getProjectionMatrix();
        projMatrix.setDump(projection);
        SimpleVector translation = projMatrix.getTranslation();

        if (!mFovSet) {
            // Calculate FOV based on projection values, but do it only once
            // For projection matrix: http://www.terathon.com/gdc07_lengyel.pdf

            float focal_len = projection[0];
            float vFov = (float) Math.atan2(1, focal_len)*2;
            mCamera.setFovAngle(vFov);

            float aspect = projection[0] / projection[5];
            float fov = vFov * aspect;
            mCamera.setYFovAngle(fov);
            mFovSet = true;
        }

        SimpleVector dir = projMatrix.getZAxis();
        SimpleVector up = projMatrix.getYAxis();
        mCamera.setPosition(translation);
        mCamera.setOrientation(dir, up);

        for (int i=0; i<mTrackableObjects.size(); i++) {
            TrackableObject3d trackableObject = mTrackableObjects.get(i);
            trackableObject.updateMarkerTransformation();
        }

        mWorld.renderScene(mBuffer);
        mWorld.draw(mBuffer);
        mBuffer.display();
    }

    /**
     * Get the world instance used by the renderer.
     * May be null if configureARScene has not yet been called.
     * @return the world instance used by the renderer.
     */
    public World getWorld() {
        return mWorld;
    }


    public SimpleVector pick(int mouseX, int mouseY)
    {
        // http://www.jpct.net/wiki/index.php?title=Picking

        SimpleVector pos = null;
        SimpleVector ray = Interact2D.reproject2D3DWS(mWorld.getCamera(), mBuffer, mouseX, mouseY);
        if (ray != null)
        {
            SimpleVector norm = ray.normalize(); // Just to be sure...

            float f = mWorld.calcMinDistance(mWorld.getCamera().getPosition(), norm, 100000);
            if (f != Object3D.COLLISION_NONE)
            {
                SimpleVector offset = new SimpleVector(norm);
                norm.scalarMul(f);
                norm = norm.calcSub(offset);
                pos = new SimpleVector(norm);
                pos.add(mWorld.getCamera().getPosition());
            }
        }
        return pos;
    }

    /**
     * Projects a 3D Position from world space into screen space.
     * @param pos3D
     * @return 2D Screen position
     */
    public SimpleVector project3Dto2D(SimpleVector pos3D) {
        return Interact2D.project3D2D(
                getCamera(),
                getBuffer(),
                pos3D);
    }
}
