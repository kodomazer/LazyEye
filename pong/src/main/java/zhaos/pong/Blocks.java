package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by ambersz on 9/4/2016.
 */
public class Blocks implements renderableObject {
    private Point center;

    public Blocks(){
        center = new Point();
    }

    private Point collidesWith(Point origin, Point velocity){
        return new Point(0,0);
    }

    public Point getPosition(){return new Point(1,2);}

    //placeholder code for rendering
    public FloatBuffer getCoords(){
        return ByteBuffer.allocateDirect( 4).asFloatBuffer();
    }
    public FloatBuffer getColors(){
        return ByteBuffer.allocateDirect( 4).asFloatBuffer();
    }
    public FloatBuffer getNormals(){
        return ByteBuffer.allocateDirect(4).asFloatBuffer();
    }

}
