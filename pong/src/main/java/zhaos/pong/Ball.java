package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Ball implements renderableObject{
    private Point puckPosition;
    private Point velocity;
    private Pong game;

    public Ball(Pong game){
        puckPosition = new Point(0,5);
        velocity = new Point(0,-1);

    }

    public void tick(){
        if(velocity.y==0||velocity.x == 0){
            //if velocity is 0,0 then game is over; need to tell game.
            //// TODO: 9/3/2016  
        }
        private float tempY = puckPosition.y +velocity.y;
        if(tempY>0 && tempY<10) {//Is ball within game range?
            puckPosition.x +=velocity.x;//Then move normally
            puckPosition.y = tempY;}
        else if (tempY<=0){ //Falls below?
            puckPosition.x += velocity.x * puckPosition.y / velocity.y;//Then bounce or die
            puckPosition.y = 0;
            game.getPlayer().bounce(velocity,puckPosition.x)
        }else if (tempY>=10){//rises to top?
            puckPosition.x += velocity.x * (10-puckPosition.y) / velocity.y;
            puckPosition.y = 10;
            game.getOpponent().bounce(velocity,puckPosition.x); //then bounce or player win

        }

    }

    public Point getPosition(){return new Point(1);}


    //placeholder code for rendering
    public FloatBuffer getCoords(){
        return ByteBuffer.allocateDirect(WorldLayoutData.FLOOR_COORDS.length * 4).asFloatBuffer();
    }
    public FloatBuffer getColors(){
        return ByteBuffer.allocateDirect(WorldLayoutData.FLOOR_COORDS.length * 4).asFloatBuffer();
    }
    public FloatBuffer getNormals(){
        return ByteBuffer.allocateDirect(WorldLayoutData.FLOOR_COORDS.length * 4).asFloatBuffer();
    }



}
