package gl.kev.ar.arengine.helper.jpct;

import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;

/**
 * Created by Kevin on 11.01.2017.
 */

public class Object3DWrapper {
    public Object3D obj;

    public Object3DWrapper(Object3D obj) {
        this.obj = obj;
    }

    public void rX(float rot) {
        obj.rotateX(rot);
    }

    public void rY(float rot) {
        obj.rotateY(rot);
    }

    public void rZ(float rot) {
        obj.rotateZ(rot);
    }

    public void resetRot() {
        obj.setRotationMatrix(new Matrix());
    }

    public void tX(float trans) {
        obj.translate(trans, 0, 0);
    }

    public void tY(float trans) {
        obj.translate(0, trans, 0);
    }

    public void tZ(float trans) {
        obj.translate(0, 0, trans);
    }

    public void resetTr() {
        obj.setTranslationMatrix(new Matrix());
    }

    public void sc(float scale) {
        obj.scale(scale);
    }

    public void resetSc() {
        obj.setScale(1.0f);
    }
}
