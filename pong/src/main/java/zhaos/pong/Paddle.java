package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Paddle extends Blocks {
    private float paddleLength;


    public Paddle(ObjectBase parent){
        super(parent);
        paddleLength = 1;
    }

    public Vector3 getPosition(){return new Vector3(1,2);}
    public float getPaddleLength(){return paddleLength;}



    public boolean collidesWith(Vector3 position, Vector3 velocity){
        //TODO Collision Check
        return false;
    }
    public void collisionResult(Vector3 position, Vector3 velocity){
        //TODO bounce angle, change position and velocity
    }




}

