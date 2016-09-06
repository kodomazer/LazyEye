package zhaos.pong;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by ambersz on 9/4/2016.
 */
public class Wall extends Blocks {//vertical wall
    private float wallHeight;
    private float x;

    public Wall(float position){
        x = position;
        wallHeight=10;
    }


    public Point getPosition(){return new Point(1,2);}

    public Point getX(){
        return new Point((int)x);
    }
    public float getWallHeight(){
        return wallHeight;
    }
    public Point bounce(Point velocity, float x){return velocity;}//should return new velocity for bounce

    // placeholder code for rendering
    public FloatBuffer getCoords(){
        return ByteBuffer.allocateDirect(4).asFloatBuffer();
    }
    public FloatBuffer getColors(){
        return ByteBuffer.allocateDirect(4).asFloatBuffer();
    }
    public FloatBuffer getNormals(){
        return ByteBuffer.allocateDirect(4).asFloatBuffer();
    }
}
