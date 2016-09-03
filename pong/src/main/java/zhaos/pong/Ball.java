package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Ball implements renderableObject{

    public Ball(){

    }


    public Point getPosition(){return new Point(1);}


    //placeholder code for rendering
    public FloatBuffer getCoords(){
        return ByteBuffer.allocateDirect(WorldLayoutData.FLOOR_COORDS.length * 4).asFloatBuffer();
    }
    public FloatBuffer getColors(){
        return ByteBuffer.allocateDirect(WorldLayoutData.FLOOR_COORDS.length * 4).asFloatBuffer();
    }
    public FloatBuffer getNormals(){
        return ByteBuffer.allocateDirect(WorldLayoutData.FLOOR_COORDS.length * 4).asFloatBuffer();
    }



}
