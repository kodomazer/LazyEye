package zhaos.pong;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by kodomazer on 9/4/2016.
 */
public class Floor extends RenderingBase {


    public Floor(){
        super();

        Matrix.translateM(model, 0, 0, -20, 0);
    }



    public void BuildModel(){
        if(!RenderResources.isGLES())return;
        // make a floor
        ByteBuffer bbFloorVertices = ByteBuffer.allocateDirect(RenderResources.FLOOR_COORDS.length * 4);
        bbFloorVertices.order(ByteOrder.nativeOrder());
        vertexBuffer = bbFloorVertices.asFloatBuffer();
        vertexBuffer.put(RenderResources.FLOOR_COORDS);
        vertexBuffer.position(0);

        ByteBuffer bbFloorNormals = ByteBuffer.allocateDirect(RenderResources.FLOOR_NORMALS.length * 4);
        bbFloorNormals.order(ByteOrder.nativeOrder());
        normalBuffer = bbFloorNormals.asFloatBuffer();
        normalBuffer.put(RenderResources.FLOOR_NORMALS);
        normalBuffer.position(0);

        ByteBuffer bbFloorColors = ByteBuffer.allocateDirect(RenderResources.FLOOR_COLORS.length * 4);
        bbFloorColors.order(ByteOrder.nativeOrder());
        colorBuffer = bbFloorColors.asFloatBuffer();
        colorBuffer.put(RenderResources.FLOOR_COLORS);
        colorBuffer.position(0);


        renderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(renderProgram, RenderResources.getShader(RenderResources.VERTEX_SHADER));
        GLES20.glAttachShader(renderProgram, RenderResources.getShader(RenderResources.GRID_SHADER));
        GLES20.glLinkProgram(renderProgram);
        GLES20.glUseProgram(renderProgram);

        RenderResources.checkGLError("Floor program");

        modelParam = GLES20.glGetUniformLocation(renderProgram, "u_Model");
        modelViewParam = GLES20.glGetUniformLocation(renderProgram, "u_MVMatrix");
        modelViewProjectionParam = GLES20.glGetUniformLocation(renderProgram, "u_MVP");
        lightPosParam = GLES20.glGetUniformLocation(renderProgram, "u_LightPos");

        positionParam = GLES20.glGetAttribLocation(renderProgram, "a_Position");
        normalParam = GLES20.glGetAttribLocation(renderProgram, "a_Normal");
        colorParam = GLES20.glGetAttribLocation(renderProgram, "a_Color");

        RenderResources.checkGLError("Floor program params");
        renderParamRows = 24;
        built=true;
    }

}
