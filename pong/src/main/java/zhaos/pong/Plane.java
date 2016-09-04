package zhaos.pong;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public class Plane {
    protected RenderItems item=RenderItems.getInstance();

    private static final int COORDS_PER_VERTEX = 3;

    private FloatBuffer floorVertices;
    private FloatBuffer floorColors;
    private FloatBuffer floorColors1;
    private FloatBuffer floorNormals;

    private int floorProgram;

    private int renderNumber;

    private int floorPositionParam;
    private int floorNormalParam;
    private int floorColorParam;
    private int floorModelParam;
    private int floorModelViewParam;
    private int floorModelViewProjectionParam;
    private int floorLightPosParam;



    public Plane() {
        renderNumber = 0;
    }

    public void initializePlane(){
        if(!RenderItems.isGLES())return;
        // make a floor
        ByteBuffer bbFloorVertices = ByteBuffer.allocateDirect(RenderItems.FLOOR_COORDS.length * 4);
        bbFloorVertices.order(ByteOrder.nativeOrder());
        floorVertices = bbFloorVertices.asFloatBuffer();
        floorVertices.put(RenderItems.FLOOR_COORDS);
        floorVertices.position(0);

        ByteBuffer bbFloorNormals = ByteBuffer.allocateDirect(RenderItems.FLOOR_NORMALS.length * 4);
        bbFloorNormals.order(ByteOrder.nativeOrder());
        floorNormals = bbFloorNormals.asFloatBuffer();
        floorNormals.put(RenderItems.FLOOR_NORMALS);
        floorNormals.position(0);

        ByteBuffer bbFloorColors = ByteBuffer.allocateDirect(RenderItems.FLOOR_COLORS.length * 4);
        bbFloorColors.order(ByteOrder.nativeOrder());
        floorColors = bbFloorColors.asFloatBuffer();
        floorColors.put(RenderItems.FLOOR_COLORS);
        floorColors.position(0);

        ByteBuffer bbFloorColors1 = ByteBuffer.allocateDirect(RenderItems.FLOOR_COLORS1.length * 4);
        bbFloorColors1.order(ByteOrder.nativeOrder());
        floorColors1 = bbFloorColors1.asFloatBuffer();
        floorColors1.put(RenderItems.FLOOR_COLORS1);
        floorColors1.position(0);


        floorProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(floorProgram, RenderItems.getShader(RenderItems.VERTEX_SHADER));
        GLES20.glAttachShader(floorProgram, RenderItems.getShader(RenderItems.GRID_SHADER));
        GLES20.glLinkProgram(floorProgram);
        GLES20.glUseProgram(floorProgram);

        RenderItems.checkGLError("Floor program");

        floorModelParam = GLES20.glGetUniformLocation(floorProgram, "u_Model");
        floorModelViewParam = GLES20.glGetUniformLocation(floorProgram, "u_MVMatrix");
        floorModelViewProjectionParam = GLES20.glGetUniformLocation(floorProgram, "u_MVP");
        floorLightPosParam = GLES20.glGetUniformLocation(floorProgram, "u_LightPos");

        floorPositionParam = GLES20.glGetAttribLocation(floorProgram, "a_Position");
        floorNormalParam = GLES20.glGetAttribLocation(floorProgram, "a_Normal");
        floorColorParam = GLES20.glGetAttribLocation(floorProgram, "a_Color");

        RenderItems.checkGLError("Floor program params");
    }


    public void Draw(float[] modelFloor,float[] modelView,float[] modelViewProjection,float[] lightPosInEyeSpace) {
        GLES20.glUseProgram(floorProgram);

        renderNumber++;

        // Set ModelView, MVP, position, normals, and color.
        GLES20.glUniform3fv(floorLightPosParam, 1, lightPosInEyeSpace, 0);
        GLES20.glUniformMatrix4fv(floorModelParam, 1, false, modelFloor, 0);
        GLES20.glUniformMatrix4fv(floorModelViewParam, 1, false, modelView, 0);
        GLES20.glUniformMatrix4fv(floorModelViewProjectionParam, 1, false, modelViewProjection, 0);
        GLES20.glVertexAttribPointer(
                floorPositionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, floorVertices);
        GLES20.glVertexAttribPointer(floorNormalParam, 3, GLES20.GL_FLOAT, false, 0, floorNormals);
        if(renderNumber%2==0)
            GLES20.glVertexAttribPointer(floorColorParam, 4, GLES20.GL_FLOAT, false, 0, floorColors);
        else
            GLES20.glVertexAttribPointer(floorColorParam, 4, GLES20.GL_FLOAT, false, 0, floorColors1);

        GLES20.glEnableVertexAttribArray(floorPositionParam);
        GLES20.glEnableVertexAttribArray(floorNormalParam);
        GLES20.glEnableVertexAttribArray(floorColorParam);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 24);

        RenderItems.checkGLError("drawing floor");

    }

}
