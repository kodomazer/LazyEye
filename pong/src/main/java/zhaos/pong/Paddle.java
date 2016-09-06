package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Paddle extends Blocks {
    private float paddleRadius;
    private float leftPaddleWall;
    private float rightPaddleWall;


    public void updatePaddlePosition(float playerSight){
        transform.x = Math.min(Math.max(playerSight, leftPaddleWall),rightPaddleWall);

    }

    public Paddle(ObjectBase parent, float y){
        super(parent);
        paddleRadius = (float) 0.5; //paddle length is twice paddle radius
        transform  = new Vector3(0,y);
        leftPaddleWall = -10 + paddleRadius;
        rightPaddleWall = 10 - paddleRadius;
    }

    public float getpaddleRadius(){return paddleRadius;}



    public boolean collidesWith(Ball b, Vector3 delta){
        if((b.transform.y - transform.y) * (b.transform.y + delta.y - transform.y) < 0){
            Vector3 intercept = b.transform.add(delta.scale((transform.y-b.transform.y)/delta.y));
            if(intercept.x<transform.x+paddleRadius && intercept.x > transform.x - paddleRadius){
                b.setPosition(intercept);
                //// TODO: 9/5/2016 update velocity after bounce 
                return true;
            }else{return false;}
        } else {return false;}
    }
//    public void collisionResult(Vector3 position, Vector3 velocity){
//        //TODO bounce angle, change position and velocity
//    }




}

