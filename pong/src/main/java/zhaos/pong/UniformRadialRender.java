package zhaos.pong;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by kodomazer on 9/4/2016.
 */
public class UniformRadialRender extends RenderingBase {
    protected float radius;
    protected int sections;

    public UniformRadialRender(){
        super();
        radius = 1;
        sections = 2;
    }
    public UniformRadialRender(float r,int s){
        super();
        radius = r==0?1:r;
        sections = s<2?2:s;
    }


    protected void BuildModel(){
        if(built)return;

        //Vertices
        ByteBuffer bbFloorVertices = ByteBuffer.allocateDirect(sections * 3 * 4);
        bbFloorVertices.order(ByteOrder.nativeOrder());
        vertexBuffer = bbFloorVertices.asFloatBuffer();
        vertexBuffer.position(0);


        //normals
        ByteBuffer bbFloorNormals = ByteBuffer.allocateDirect(sections *3 * 4);
        bbFloorNormals.order(ByteOrder.nativeOrder());
        normalBuffer = bbFloorNormals.asFloatBuffer();

        normalBuffer.position(0);
        //Colors
        ByteBuffer bbFloorColors = ByteBuffer.allocateDirect(sections *4 * 4);
        bbFloorColors.order(ByteOrder.nativeOrder());
        colorBuffer = bbFloorColors.asFloatBuffer();
        for(int i =0;i<6;i++){
            colorBuffer.put(new float[] {0.74f,0.75f,0.0f,0.75f});
        }
        colorBuffer.position(0);



        //program
        renderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(renderProgram, RenderResources.getShader(RenderResources.VERTEX_SHADER));
        GLES20.glAttachShader(renderProgram, RenderResources.getShader(RenderResources.PASSTHROUGH_SHADER));
        GLES20.glLinkProgram(renderProgram);
        GLES20.glUseProgram(renderProgram);

        RenderResources.checkGLError("Floor program");

        //param locations
        modelParam = GLES20.glGetUniformLocation(renderProgram, "u_Model");
        modelViewParam = GLES20.glGetUniformLocation(renderProgram, "u_MVMatrix");
        modelViewProjectionParam = GLES20.glGetUniformLocation(renderProgram, "u_MVP");
        lightPosParam = GLES20.glGetUniformLocation(renderProgram, "u_LightPos");

        positionParam = GLES20.glGetAttribLocation(renderProgram, "a_Position");
        normalParam = GLES20.glGetAttribLocation(renderProgram, "a_Normal");
        colorParam = GLES20.glGetAttribLocation(renderProgram, "a_Color");

        RenderResources.checkGLError("Floor program params");
        renderParamRows = 6;
        built = true;



    }


}
