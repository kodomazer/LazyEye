package zhaos.pong;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by ambersz on 9/4/2016.
 * vertical wall
 */
public class Wall extends Blocks {
    private Vector3 norm = new Vector3(1);

    public Wall(ObjectBase parent,float position){
        super(parent);
        transform.x = position;
        thickness = 0.5f;

        Quad wall = new Quad(this);
        wall.setVertices(
                new Vector3(-thickness,10),
                new Vector3(thickness,10),
                new Vector3(thickness,-10),
                new Vector3(-thickness,-10));
        render = wall;
    }


    public boolean collidesWith(Ball b, Vector3 delta){
        float collisionDistance;
        if(b.getVelocity().x<0){
            collisionDistance = b.transform.x-transform.x - thickness - b.getRadius();
        } else if(b.getVelocity().x>0){
            collisionDistance = b.transform.x-transform.x + thickness + b.getRadius();
        } else{
            return false;
        }

        if((collisionDistance) * (collisionDistance + delta.x) < 0){
            Vector3 intercept = b.transform.add(delta.scale(-(collisionDistance)/delta.x));
            b.setPosition(intercept);
            b.setVelocity(b.getVelocity().reflectOverNorm(norm)); // update velocity after bounce
            return true;
        } else {return false;}
    }
}
