package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Ball implements renderableObject{

    private FloatBuffer vertexCoordinates;
    private FloatBuffer normals;
    private FloatBuffer colors;





    public Ball(){

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


    public Point getPosition(){return new Point(1);}


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
