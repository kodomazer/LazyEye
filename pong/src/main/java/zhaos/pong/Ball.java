package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Ball implements renderableObject{
    private Point puckPosition;
    private Point velocity;

    public Ball(){
        puckPosition = new Point(0,5);
        velocity = new Point(0,-1);

    }

    public void tick(float deltaT){// Do I need this tick? Maybe leave it in pong
        private Point delta = new Point(velocity.x * deltaT, velocity.y * deltaT);
        float tempY;
        tempY = puckPosition.y +delta.y;


    }

    public Point getPosition(){return new Point(1);}
    public boolean isDead(){//Is the ball dead?
        if(velocity.y==0){
            return true;
        }
        else return false;

    }


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
