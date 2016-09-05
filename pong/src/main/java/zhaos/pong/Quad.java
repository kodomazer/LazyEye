package zhaos.pong;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by kodomazer on 9/4/2016.
 */
public class Quad extends RenderingBase {

    protected Vector3[] vertexes;

    Quad(){
        super();
        vertexes = new Vector3[4];
        for (int i =0;i<4;i++){
            vertexes[i]=new Vector3();
        }
    }
    Quad(ObjectBase obj){
        super(obj);
        vertexes = new Vector3[4];
        for (int i =0;i<4;i++){
            vertexes[i]=new Vector3();
        }
    }


    public void setVertices(Vector3 point1, Vector3 point2, Vector3 point3, Vector3 point4) {
        vertexes[0] = new Vector3(point1);
        vertexes[1] = new Vector3(point2);
        vertexes[2] = new Vector3(point3);
        vertexes[3] = new Vector3(point4);
        built = false;
        if (!RenderResources.isGLES()) return;
        BuildModel();
    }


    protected void BuildModel(){
        //Vertices
        ByteBuffer bbFloorVertices = ByteBuffer.allocateDirect(6 * 3 * 4);
        bbFloorVertices.order(ByteOrder.nativeOrder());
        vertexBuffer = bbFloorVertices.asFloatBuffer();
        for(int i = 0;i<3;i++) {
            vertexBuffer.put(vertexes[i].x);
            vertexBuffer.put(vertexes[i].y);
            vertexBuffer.put(vertexes[i].z);
        }
        for(int i = 0;i<3;i++) {
            vertexBuffer.put(vertexes[(i+2)%4].x);
            vertexBuffer.put(vertexes[(i+2)%4].y);
            vertexBuffer.put(vertexes[(i+2)%4].z);
        }
        vertexBuffer.position(0);


        //normals
        ByteBuffer bbFloorNormals = ByteBuffer.allocateDirect(6*3 * 4);
        bbFloorNormals.order(ByteOrder.nativeOrder());
        normalBuffer = bbFloorNormals.asFloatBuffer();
        Vector3 a = vertexes[0].subtract(vertexes[1]).cross(vertexes[0].subtract(vertexes[2]));
        for (int i =0;i<3;i++) {
            normalBuffer.put(a.x);
            normalBuffer.put(a.y);
            normalBuffer.put(a.z);
    }
        a = vertexes[2].subtract(vertexes[3]).cross(vertexes[2].subtract(vertexes[0]));
        for (int i =0;i<3;i++) {
            normalBuffer.put(a.x);
            normalBuffer.put(a.y);
            normalBuffer.put(a.z);
        }
        normalBuffer.position(0);
        //Colors
        ByteBuffer bbFloorColors = ByteBuffer.allocateDirect(6*4 * 4);
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


    @Override
    public void Draw(float[] view, float[] perspective, float[] lightPosInEyeSpace) {

        super.Draw(view, perspective, lightPosInEyeSpace);
    }
}
