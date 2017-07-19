package gl.kev.ar.arengine.helper.jpct;


import android.content.Context;

import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;

/**
 * Created by Kevin on 12.08.2016.
 */
public class JPCTHelper {

    public static void init(Context context) {
        TextureManager.getInstance().addTexture("RED", new Texture(8, 8, RGBColor.RED));
        TextureManager.getInstance().addTexture("BLUE", new Texture(8, 8, RGBColor.BLUE));
        TextureManager.getInstance().addTexture("GREEN", new Texture(8, 8, RGBColor.GREEN));

        try {
            //TextureManager.getInstance().addTexture("ABB", new Texture(context.getResources().getAssets().open("abb.png"), true));
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public static Object3D createLine (SimpleVector pointA, SimpleVector pointB, float width, String textureName)
    {
        Object3D line = new Object3D( 8 );
        float offset = width / 2.0f;

        // Quad A:
        line.addTriangle( new SimpleVector( pointA.x, pointA.y - offset, pointA.z ), 0, 0,
                new SimpleVector( pointA.x, pointA.y + offset, pointA.z ), 0, 1,
                new SimpleVector( pointB.x, pointB.y + offset, pointB.z ), 1, 1,
                TextureManager.getInstance().getTextureID( textureName ) );
        line.addTriangle( new SimpleVector( pointB.x, pointB.y + offset, pointB.z ), 0, 0,
                new SimpleVector( pointB.x, pointB.y - offset, pointB.z ), 0, 1,
                new SimpleVector( pointA.x, pointA.y - offset, pointA.z ), 1, 1,
                TextureManager.getInstance().getTextureID( textureName ) );
        // Quad A, back-face:
        line.addTriangle( new SimpleVector( pointB.x, pointB.y - offset, pointB.z ), 0, 0,
                new SimpleVector( pointB.x, pointB.y + offset, pointB.z ), 0, 1,
                new SimpleVector( pointA.x, pointA.y + offset, pointA.z ), 1, 1,
                TextureManager.getInstance().getTextureID( textureName ) );
        line.addTriangle( new SimpleVector( pointA.x, pointA.y + offset, pointA.z ), 0, 0,
                new SimpleVector( pointA.x, pointA.y - offset, pointA.z ), 0, 1,
                new SimpleVector( pointB.x, pointB.y - offset, pointB.z ), 1, 1,
                TextureManager.getInstance().getTextureID( textureName ) );
        // Quad B:
        line.addTriangle( new SimpleVector( pointA.x, pointA.y, pointA.z + offset ), 0, 0,
                new SimpleVector( pointA.x, pointA.y, pointA.z - offset ), 0, 1,
                new SimpleVector( pointB.x, pointB.y, pointB.z - offset ), 1, 1,
                TextureManager.getInstance().getTextureID( textureName ) );
        line.addTriangle( new SimpleVector( pointB.x, pointB.y, pointB.z - offset ), 0, 0,
                new SimpleVector( pointB.x, pointB.y, pointB.z + offset ), 0, 1,
                new SimpleVector( pointA.x, pointA.y, pointA.z + offset ), 1, 1,
                TextureManager.getInstance().getTextureID( textureName ) );
        // Quad B, back-face:
        line.addTriangle( new SimpleVector( pointB.x, pointB.y, pointB.z + offset ), 0, 0,
                new SimpleVector( pointB.x, pointB.y, pointB.z - offset ), 0, 1,
                new SimpleVector( pointA.x, pointA.y, pointA.z - offset ), 1, 1,
                TextureManager.getInstance().getTextureID( textureName ) );
        line.addTriangle( new SimpleVector( pointA.x, pointA.y, pointA.z - offset ), 0, 0,
                new SimpleVector( pointA.x, pointA.y, pointA.z + offset ), 0, 1,
                new SimpleVector( pointB.x, pointB.y, pointB.z + offset ), 1, 1,
                TextureManager.getInstance().getTextureID( textureName ) );

        // If you don't want the line to react to lighting:
        line.setLighting( Object3D.LIGHTING_NO_LIGHTS );
        line.setAdditionalColor( RGBColor.WHITE );

        // done
        return line;
    }

    public static Object3D createKartesianGizmo(float size, float width) {
        return new Node3D(new Object3D[] {
                createLine(SimpleVector.ORIGIN, SimpleVector.create(size, 0, 0),width, "RED"),
                createLine(SimpleVector.ORIGIN, SimpleVector.create(0, size, 0),width, "GREEN"),
                createLine(SimpleVector.ORIGIN, SimpleVector.create(0, 0, size),width, "BLUE")
        });
    }

    public static void createKartesianGizmo(float size, float width, Object3D node) {
        node.addChild(createLine(SimpleVector.ORIGIN, SimpleVector.create(size, 0, 0),width, "RED"));
        node.addChild(createLine(SimpleVector.ORIGIN, SimpleVector.create(0, size, 0),width, "GREEN"));
        node.addChild(createLine(SimpleVector.ORIGIN, SimpleVector.create(0, 0, size),width, "BLUE"));
    }

    /*public static void lookAt( Object3D object, SimpleVector target )
    {
        SimpleVector direction = new SimpleVector( target.calcSub( object.getTransformedCenter() ) ).normalize();
        Matrix rotationMatrix = new Matrix( direction.getRotationMatrix() );
        object.setRotationMatrix( rotationMatrix );
    }*/

    public static void lookAt( Object3D object, SimpleVector target )
    {
        float initialScale = object.getScale();
        object.setScale( 1.0f );
        SimpleVector direction = new SimpleVector(
                target.calcSub( object.getTransformedCenter() ) ).normalize();
        Matrix rotationMatrix = new Matrix( direction.getRotationMatrix() );
        object.setRotationMatrix( rotationMatrix );
        object.setScale( initialScale );
    }
}
