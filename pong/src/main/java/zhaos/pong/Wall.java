package zhaos.pong;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by ambersz on 9/4/2016.
 * vertical wall
 */
public class Wall extends Blocks {

    public Wall(ObjectBase parent,float position){
        super(parent);
        transform.x = position;

        Quad wall = new Quad(this);
        wall.setVertices(
                new Vector3(-2,5),
                new Vector3(2,5),
                new Vector3(2,-5),
                new Vector3(-2,-5));
        render = wall;
    }

    public boolean collidesWith(Vector3 position, Vector3 velocity){
        //TODO Collision Check
        return false;
    }
    public void collisionResult(Vector3 position, Vector3 velocity){
        //TODO bounce angle, change position and velocity
    }

}
