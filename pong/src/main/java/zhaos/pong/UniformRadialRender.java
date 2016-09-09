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
        uniformColor =new float[] {0.75f,0.75f,0.0f,0.75f};
    }
    public UniformRadialRender(ObjectBase a){
        super();
        baseObject = a;
        radius = 1;
        sections = 2;
        uniformColor =new float[] {0.75f,0.75f,0.0f,0.75f};
    }
    public UniformRadialRender(float r,int s){
        super();
        radius = r>0?1:r;
        sections = s<2?2:s;
        uniformColor =new float[] {0.75f,0.75f,0.0f,0.75f};
    }

    public void setRadius(float a){
        radius = a>0?a:radius;
        built=false;
    }
    public void setSections(int a){
        sections = a>1?a:sections;
        built=false;
    }


    protected void BuildModel(){
        if(built)return;

        //Vertices
        ByteBuffer bbFloorVertices = ByteBuffer.allocateDirect(sections*3 * 3 * 4);
        bbFloorVertices.order(ByteOrder.nativeOrder());
        vertexBuffer = bbFloorVertices.asFloatBuffer();
        float left=0;
        Vector3 first = new Vector3();
        Vector3 second = new Vector3();
        float right = 0;
        Vector3 third = new Vector3();
        for(int i=0;i<sections;i++){
            left = right;
            right = (360/sections)*(i+1);
            second.x = (float)Math.cos(Math.toRadians(left))*radius;
            second.y = (float)Math.sin(Math.toRadians(left))*radius;
            third.x = (float)Math.cos(Math.toRadians(right))*radius;
            third.y = (float)Math.sin(Math.toRadians(right))*radius;
            vertexBuffer.put(first.x);
            vertexBuffer.put(first.y);
            vertexBuffer.put(first.z);
            vertexBuffer.put(second.x);
            vertexBuffer.put(second.y);
            vertexBuffer.put(second.z);
            vertexBuffer.put(third.x);
            vertexBuffer.put(third.y);
            vertexBuffer.put(third.z);
        }

        vertexBuffer.position(0);


        //normals
        ByteBuffer bbFloorNormals = ByteBuffer.allocateDirect(sections*3 *3 * 4);
        bbFloorNormals.order(ByteOrder.nativeOrder());
        normalBuffer = bbFloorNormals.asFloatBuffer();
        for(int i = 0;i<sections*3;i++)
            normalBuffer.put(new float[]{0,0,-1});
        normalBuffer.position(0);
        //Colors
        ByteBuffer bbFloorColors = ByteBuffer.allocateDirect(sections*3 *4 * 4);
        bbFloorColors.order(ByteOrder.nativeOrder());
        colorBuffer = bbFloorColors.asFloatBuffer();
        for(int i =0;i<sections*3;i++){
            colorBuffer.put(uniformColor);
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
        renderParamRows = sections*3;
        built = true;
    }


}
