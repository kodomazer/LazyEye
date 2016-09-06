package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Ball extends ObjectBase{
    protected Vector3 velocity;


    protected boolean active;

    public Ball(ObjectBase parent){
        super(parent);
        transform = new Vector3(0,5);
        velocity = new Vector3(0,-1);
        active = true;

        UniformRadialRender ball = new UniformRadialRender(this);
        ball.setSections(20);
        ball.setRadius(1);
        render = ball;
    }

    public boolean isActive(){ return active;}

//    public void tick(float deltaT){// Do I need this tick? Maybe leave it in pong
//        private Vector3 delta = new Vector3(velocity.x * deltaT, velocity.y * deltaT);
//        float tempY;
//        tempY = puckPosition.y +delta.y;
//
//
//    }

    public Vector3 getVelocity(){
        return velocity;
    }

    public void move(float deltaT){
        super.translate(velocity.scale(deltaT));
    }
}
