package zhaos.pong;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static zhaos.pong.Pong.getGame;


/**
 * Created by ambersz on 9/4/2016.
 * vertical wall
 */
public class Wall extends Blocks {
    private Vector3 norm = new Vector3(1);
    private int isRightWall;

    public Wall(ObjectBase parent,float position){
        super(parent);
        transform.x = position;
        if(position>0){
            isRightWall = 1;
        }else {
            isRightWall = -1;
        }
        thickness = 0.5f;

        Quad wall = new Quad(this);
        wall.setVertices(
                new Vector3(-thickness,getGame().getGameHeight()),
                new Vector3(thickness,getGame().getGameHeight()),
                new Vector3(thickness,-getGame().getGameHeight()),
                new Vector3(-thickness,-getGame().getGameHeight()));
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

        if(isRightWall*collisionDistance>0 || isRightWall * (collisionDistance+delta.x) >0){

//        if((collisionDistance) * (collisionDistance + delta.x) < 0){
            Vector3 intercept = b.transform.add(delta.scale(-(collisionDistance)/delta.x));
            b.setPosition(intercept);
            b.setVelocity(b.getVelocity().reflectOverNorm(norm)); // update velocity after bounce
            return true;
        } else {return false;}
    }
}
