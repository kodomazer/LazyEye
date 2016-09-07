package zhaos.pong;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Paddle extends Blocks {
    private float paddleRadius;
    private float leftPaddleWall;
    private float rightPaddleWall;
    private float curvature; // curvature of paddle for bounce purposes
    protected float paddleThickness;


    public void updatePaddlePosition(float playerSight){
        transform.x = Math.min(Math.max(playerSight, leftPaddleWall),rightPaddleWall);

    }

    public Paddle(ObjectBase parent, float y){
        super(parent);
        paddleRadius = (float) 5; //paddle length is twice paddle radius
        transform  = new Vector3(0,y);
        leftPaddleWall = -10 + paddleRadius;
        rightPaddleWall = 10 - paddleRadius;
        curvature = (float) Math.PI / 6 / paddleRadius;
        paddleThickness = 1.0f;

        Quad paddle = new Quad(this);
        paddle.setVertices(
                new Vector3(-paddleRadius,paddleThickness),
                new Vector3(paddleRadius,paddleThickness),
                new Vector3(paddleRadius,-paddleThickness),
                new Vector3(-paddleRadius,-paddleThickness));
        render = paddle;
    }

//    public float getpaddleRadius(){return paddleRadius;}

    private Vector3 getBounceNorm(float offsetFromCenter){
        return new Vector3((float) Math.sin(offsetFromCenter*curvature), (float) Math.cos(offsetFromCenter * curvature));
    }

    public boolean collidesWith(Ball b, Vector3 delta){
        float contactPointOffset;

        if (b.transform.y > transform.y) {
            contactPointOffset = -b.getRadius()-paddleThickness;
        }else if(b.transform.y<transform.y){
            contactPointOffset = b.getRadius()+paddleThickness;
        }else {
            return false;
        }

        if((b.transform.y + contactPointOffset - transform.y) * (b.transform.y + contactPointOffset + delta.y - transform.y) < 0){


            Vector3 intercept = b.transform.add(delta.scale((transform.y-(b.transform.y+ contactPointOffset))/delta.y));
            if(intercept.x<transform.x+paddleRadius && intercept.x > transform.x - paddleRadius){
                b.setPosition(intercept);

                b.setVelocity(b.getVelocity().reflectOverNorm(getBounceNorm(intercept.x - transform.x))); //update velocity after bounce

                return true;
            }else{return false;}
        } else {return false;}
    }
}

