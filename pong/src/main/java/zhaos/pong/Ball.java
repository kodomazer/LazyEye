package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Ball extends ObjectBase{
    private Vector3 puckPosition;
    private Vector3 velocity;
    private boolean active;

    private FloatBuffer vertexCoordinates;
    private FloatBuffer normals;
    private FloatBuffer colors;





    public Ball(){
        puckPosition = new Vector3(0,5);
        velocity = new Vector3(0,-1);
        active = true;

        //Vertices
        ByteBuffer bbVertices = ByteBuffer.allocateDirect(BALL_VERTICES.length * 4);
        bbVertices.order(ByteOrder.nativeOrder());
        vertexCoordinates = bbVertices.asFloatBuffer();
        vertexCoordinates.put(BALL_VERTICES);
        vertexCoordinates.position(0);

        //Normals
        ByteBuffer bbNormals = ByteBuffer.allocateDirect(RenderResources.FLOOR_NORMALS.length * 4);
        bbNormals.order(ByteOrder.nativeOrder());
        normals = bbNormals.asFloatBuffer();
        normals.put(RenderResources.FLOOR_NORMALS);
        normals.position(0);

        //Colors
        ByteBuffer bbColors = ByteBuffer.allocateDirect(RenderResources.FLOOR_COLORS.length * 4);
        bbColors.order(ByteOrder.nativeOrder());
        colors = bbColors.asFloatBuffer();
        colors.put(RenderResources.FLOOR_COLORS);
        colors.position(0);

    }

    public boolean isActive(){ return active;}

    public



//    public void tick(float deltaT){// Do I need this tick? Maybe leave it in pong
//        private Vector3 delta = new Vector3(velocity.x * deltaT, velocity.y * deltaT);
//        float tempY;
//        tempY = puckPosition.y +delta.y;
//
//
//    }

    public Vector3 getPosition(){return puckPosition;}
    public Vector3 getVelocity(){return velocity;}

    public void move(float deltaT){
        puckPosition = puckPosition.add(velocity.scale(deltaT));
    }



    //placeholder code for rendering
    public FloatBuffer getCoords(){
        return vertexCoordinates;
    }
    public FloatBuffer getColors(){
        return colors;
    }
    public FloatBuffer getNormals(){
        return normals;
    }


    private final float[] BALL_VERTICES={
            0,0,0,
            0,0,0,
    };

    private final float[] BALL_NORMALS={
            0,0,0,
            0,0,0,
    };

    private final float[] BALL_COLORS={
            0,0,0,
            0,0,0,
    };



}
