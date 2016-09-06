package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by ambersz on 9/4/2016.
 */
public class Blocks extends ObjectBase {
    private Vector3 center;

    public Blocks(){
        center = new Vector3();
    }


    public boolean collidesWith(Vector3 origin, Vector3 velocity) {
        //TODO put im collision check
        return false;
    }
    //placeholder code for rendering
    public FloatBuffer getCoords(){
        return ByteBuffer.allocateDirect( 4).asFloatBuffer();
    }
    public FloatBuffer getColors(){
        return ByteBuffer.allocateDirect( 4).asFloatBuffer();
    }
    public FloatBuffer getNormals(){
        return ByteBuffer.allocateDirect(4).asFloatBuffer();
    }


}
