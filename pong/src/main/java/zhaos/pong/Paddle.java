package zhaos.pong;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static zhaos.pong.Pong.getGame;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Paddle extends Blocks {
    private float paddleRadius;
    private float leftPaddleWall;
    private float rightPaddleWall;
    private float curvature; // curvature of paddle for bounce purposes



    public void updatePaddlePosition(float playerSight){
        transform.x = Math.min(Math.max(playerSight, leftPaddleWall),rightPaddleWall);

    }

    public Paddle(ObjectBase parent, float y, float radius){
        super(parent);
        paddleRadius = (float) radius; //paddle length is twice paddle radius
        thickness = 1.0f;
        transform  = new Vector3(0,Math.copySign(Math.abs(y)-thickness,y));
        leftPaddleWall = -getGame().getGameWidth() + paddleRadius;
        rightPaddleWall = getGame().getGameWidth() - paddleRadius;
        curvature = (float) Math.PI / 12 / paddleRadius;

        Quad paddle = new Quad(this);
        paddle.setVertices(
                new Vector3(-paddleRadius,thickness),
                new Vector3(paddleRadius,thickness),
                new Vector3(paddleRadius,-thickness),
                new Vector3(-paddleRadius,-thickness));
        render = paddle;
    }

//    public float getpaddleRadius(){return paddleRadius;}

    private Vector3 getBounceNorm(float offsetFromCenter){
        return new Vector3((float) Math.sin(offsetFromCenter*curvature), (float) Math.cos(offsetFromCenter * curvature));
    }

    public boolean collidesWith(Ball b, Vector3 delta) {


        float contactPointOffset;
        float ballToPaddle = b.transform.y - transform.y; //distance from center of ball to center of paddle, positive if ball above, negative if ball below


        if (b.getVelocity().y<0) {
            contactPointOffset = -1;
        } else if (b.getVelocity().y>0) {
            contactPointOffset = 1;
        } else {
            return false;
        }

//        for(Wall w:getGame().getWalls()){
//
//        }

//        if (Math.abs(ballToPaddle + contactPointOffset * b.getRadius()) < thickness) {//if contact point is inside paddle, no collision
//            return false;
//        }


        contactPointOffset *= b.getRadius() + thickness;

        if ((ballToPaddle + contactPointOffset) * (ballToPaddle + contactPointOffset + delta.y) < 0) {


            Vector3 intercept = b.transform.add(delta.scale(-(ballToPaddle + contactPointOffset) / delta.y));
            if (Math.abs(intercept.x - transform.x) < paddleRadius) {
                b.setPosition(intercept);

                b.setVelocity(b.getVelocity().reflectOverNorm(getBounceNorm(intercept.x - transform.x)).scale(1.1f)); //update velocity after bounce

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

