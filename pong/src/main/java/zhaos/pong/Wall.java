package zhaos.pong;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by ambersz on 9/4/2016.
 */
public class Wall extends Blocks {//vertical wall
    private float wallHeight;
    private float x;

    public Paddle(float position){
        x = position;
        wallHeight=10;
    }

    public Point getX(){
        return x;
    }
    public float getWallHeight(){
        return wallHeight;
    }
    public Point bounce(Point velocity, float x){return velocity}//should return new velocity for bounce
}
