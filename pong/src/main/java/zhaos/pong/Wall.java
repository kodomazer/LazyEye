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

        Quad wall = new Quad(this);
        wall.setVertices(
                new Vector3(-0.5f,10),
                new Vector3(0.5f,10),
                new Vector3(0.5f,-10),
                new Vector3(-0.5f,-10));
        render = wall;
    }


    public boolean collidesWith(Ball b, Vector3 delta){
        if((b.transform.x - transform.x) * (b.transform.x + delta.x - transform.x) < 0){
            Vector3 intercept = b.transform.add(delta.scale((transform.x-b.transform.x)/delta.x));
            b.setPosition(intercept);
            b.setVelocity(b.getVelocity().reflectOverNorm(norm)); // update velocity after bounce
            return true;
        } else {return false;}
    }
}
