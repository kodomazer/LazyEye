package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Ball extends ObjectBase {
    protected Vector3 velocity;
    protected boolean active;
    protected float radius;



    public Ball(ObjectBase parent) {
        super(parent);
        transform = new Vector3(0, 2);
        velocity = new Vector3(0, -5);
        radius =1;
        active = true;

        UniformRadialRender ball = new UniformRadialRender(this);
        ball.setSections(20);
        ball.setRadius(radius);
        render = ball;
    }

    public boolean isActive() {
        return active;
    }
    public void kill(){//kill ball when collides with goals
        active = false;
    }


    public Vector3 getVelocity() {
        return velocity;
    }

    public void move(float deltaT) {
        super.translate(velocity.scale(deltaT));
    }

    public void setVelocity(Vector3 newVelocity){
        velocity = newVelocity;
    }

    public void speedUp(float modifier){ //multiplicative modifier (if double the speed then use modifier of 2)
        velocity = velocity.scale(modifier);
    }

    public float getRadius(){
        return radius;
    }

}
