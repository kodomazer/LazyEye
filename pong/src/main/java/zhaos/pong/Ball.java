package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Ball extends ObjectBase{
    private Vector3 puckPosition;
    private Vector3 velocity;
    private boolean active;







    public Ball(){
        puckPosition = new Vector3(0,5);
        velocity = new Vector3(0,-1);
        active = true;


    }


    public boolean isActive(){ return active;}

//    public void tick(float deltaT){// Do I need this tick? Maybe leave it in pong
//        private Vector3 delta = new Vector3(velocity.x * deltaT, velocity.y * deltaT);
//        float tempY;
//        tempY = puckPosition.y +delta.y;
//
//
//    }

    public Vector3 getPosition(){return puckPosition;}
    public Vector3 getVelocity(){return velocity;}

    public void move(float deltaT){
        puckPosition = puckPosition.add(velocity.scale(deltaT));
    }


    //placeholder code for rendering
    public FloatBuffer getCoords(){
        return ByteBuffer.allocateDirect(4).asFloatBuffer();
    }
    public FloatBuffer getColors(){
        return ByteBuffer.allocateDirect(4).asFloatBuffer();
    }
    public FloatBuffer getNormals(){
        return ByteBuffer.allocateDirect(4).asFloatBuffer();
    }


    private final float[] BALL_VERTICES={
            0,0,0,
            0,0,0,
    };

    private final float[] BALL_NORMALS={
            0,0,0,
            0,0,0,
    };

    private final float[] BALL_COLORS={
            0,0,0,
            0,0,0,
    };



}
