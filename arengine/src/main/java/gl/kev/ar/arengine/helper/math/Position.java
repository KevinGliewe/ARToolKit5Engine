package gl.kev.ar.arengine.helper.math;


import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Locale;

/**
 * Created by Kevin on 07.08.2016.
 */
public class Position {
    private Vector3f _v;
    private Quaternionf _q;

    public Position() {
        this._v = new Vector3f();
        this._q = new Quaternionf();
    }

    public Position(Position pos) {
        this._v = new Vector3f(pos._v);
        this._q = new Quaternionf(pos._q);
    }

    public Position(float x, float y, float z) {
        this._v = new Vector3f(x, y, z);
        this._q = new Quaternionf();
    }

    public Position(Vector3f v, Quaternionf q) {
        this._v = v;
        this._q = q;
    }

    public Position(Matrix4f m) {
        this._v = m.getTranslation(new Vector3f());
        this._q = m.getNormalizedRotation(new Quaternionf());
    }

    public Vector3f getV() {
        return _v;
    }

    public Quaternionf getQ() {
        return _q;
    }

    public Position translate(float x, float y, float z) {
        Position p = this.clone();
        p._v.add(x,y,z, p._v);
        return  p;
    }

    public Position getDiff(Position to, Position dest) {
        if(dest == null)
            dest = new Position();

        this._v.sub(to._v, dest._v);
        this._q.difference(to._q, dest._q);

        // Reverse rotate kartesian Koordinates
        Quaternionf inv = this._q.invert(new Quaternionf());

        dest._v.rotate(inv, dest._v);

        return dest;
    }

    public Position add(Position to, Position dest) {
        if(dest == null)
            dest = new Position();

        Vector3f vdiff = to._v.rotate(this._q, new Vector3f());

        this._v.sub(vdiff, dest._v);
        this._q.mul(to._q, dest._q);

        return dest;
    }

    public Position add(Position to) { return add(to, null); }

    public Position invert(Position dest) {
        if(dest == null)
            dest = new Position();

        dest._v = this._v.mul(-1);
        dest._q = this._q.invert();
        return dest;
    }

    public Position invert() { return invert(null); }

    public Matrix4f toMatrix4f(Matrix4f dest) {
        if(dest == null)
            dest = new Matrix4f();

        dest.set(this._q);
        dest.setTranslation(this._v);

        return dest;
    }

    public Position clone() {
        return new Position(new Vector3f(_v), new Quaternionf(_q));
    }

    public void apply(Object3D object3d) {
        Matrix projMatrix = new Matrix();

        float[] transformation = this.toMatrix4f(null).toArray(null);
        projMatrix.setDump(transformation);
        object3d.clearTranslation();
        object3d.translate(projMatrix.getTranslation());
        object3d.setRotationMatrix(projMatrix);
    }


    @Override
    public String toString() {
        return String.format("V: %s | Q: %s", this._v, this._q);
    }

    public String toABB(String name) {
        return String.format( Locale.ROOT,
                "CONST robtarget p%s := [ [%f, %f, %f], [%f, %f, %f, %f], [1, 1, 0, 0], [ 9E9, 9E9, 9E9, 9E9, 9E9, 9E9] ];",
                name,
                _v.x,
                _v.y,
                _v.z,
                _q.x,
                _q.y,
                _q.z,
                _q.w
        );

        /*
        https://library.e.abb.com/public/688894b98123f87bc1257cc50044e809/Technical%20reference%20manual_RAPID_3HAC16581-1_revJ_en.pdf
        < dataobject of robtarget >
            < trans of pos >
                < x of num >
                < y of num >
                < z of num >
            < rot of orient >
                < q1 of num >
                < q2 of num >
                < q3 of num >
                < q4 of num >
            < robconf of confdata >
                < cf1 of num >
                < cf4 of num >
                < cf6 of num >
                < cfx of num >
            < extax of extjoint >
                < eax_a of num >
                < eax_b of num >
                < eax_c of num >
                < eax_d of num >
                < eax_e of num >
                < eax_f of num >
        */
    }
}
