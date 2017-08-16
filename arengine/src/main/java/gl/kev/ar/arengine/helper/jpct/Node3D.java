package gl.kev.ar.arengine.helper.jpct;


import com.threed.jpct.Object3D;
import com.threed.jpct.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kevin on 20.08.2016.
 */
public class Node3D extends Object3D {

    private List<Object3D> mChildren = new ArrayList<>();

    private World mWorld;

    public Node3D() {
        super(2);
    }
    public Node3D(boolean debug) {
        super(2);
        JPCTHelper.createKartesianGizmo(50, 5, this);
    }


    public Node3D(Object3D[] children) {
        super(2);
        for(Object3D o : children)
            addChild(o);
    }

    @Override
    public void setVisibility (boolean visible) {
        super.setVisibility(visible);
        for (int i=0; i<mChildren.size(); i++) {
            mChildren.get(i).setVisibility(visible);
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
        mWorld = world;
        world.addObject(this);
        for (int i=0; i<mChildren.size(); i++) {
            Object3D o = mChildren.get(i);
            if(o instanceof Node3D)
                ((Node3D)o).addToWorld(world);
            else
                world.addObject(o);
        }
    }

    public void removeFromWorld() {
        if(mWorld == null)
            return;

        mWorld.removeObject(this);
        for (int i=0; i<mChildren.size(); i++) {
            mWorld.removeObject(mChildren.get(i));
        }
        mWorld = null;
    }

    @Override
    public void setTransparency(int trans) {
        super.setTransparency(trans);
        for (int i=0; i<mChildren.size(); i++) {
            mChildren.get(i).setTransparency(trans);
        }
    }

    @Override
    public void setTransparencyMode(int mode) {
        super.setTransparencyMode(mode);
        for (int i=0; i<mChildren.size(); i++) {
            mChildren.get(i).setTransparencyMode(mode);
        }
    }
}
