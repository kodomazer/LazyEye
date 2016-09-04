package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Paddle implements renderableObject{
    private float paddleLength;

    public Paddle(){
        paddleLength = 1;
    }

    public Point getPosition(){return new Point(1,2);}
    public float getPaddleLength(){return paddleLength;}
    public Point bounce(Point velocity, float x){return velocity}//should return new velocity for bounce or (0,0) if paddle missed


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

