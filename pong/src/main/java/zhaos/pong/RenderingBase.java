package zhaos.pong;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 *
 * Rendering Base is the base class for anything that is rendered,
 * it is always "attached" to an Object Base
 */
public abstract class RenderingBase {
    protected ObjectBase baseObject;

    protected RenderResources item= RenderResources.getInstance();

    private static final int COORDS_PER_VERTEX = 3;

    protected FloatBuffer vertexBuffer;
    protected FloatBuffer colorBuffer;
    protected FloatBuffer normalBuffer;

    protected float[] uniformColor;

    protected int renderProgram;

    protected int renderNumber;

    protected int positionParam;
    protected int normalParam;
    protected int colorParam;
    protected int modelParam;
    protected int modelViewParam;
    protected int modelViewProjectionParam;
    protected int lightPosParam;

    protected float[] model;
    protected float[] modelView;
    private float[] modelViewProjection;

    protected int renderParamRows;
    protected boolean built = false;

    public RenderingBase() {
        model = new float[16];
        modelView = new float[16];
        modelViewProjection = new float[16];

        uniformColor = new float[4];

        Matrix.setIdentityM(model, 0);

        renderNumber = 0;
        renderParamRows = 0;
        built = false;
        baseObject = ObjectBase.getEmpty();
    }

    public RenderingBase(ObjectBase base){
        this();
        baseObject=base;

    }

    protected abstract void BuildModel();

    public void setUniformColor(float[] color){
        if(color.length==4)
            uniformColor = color;
        built = false;
    }

    public void Draw(float[] view,
                     float[] perspective,
                     float[] lightPosInEyeSpace)
    {
        if(!built)BuildModel();
        renderNumber++;
        GLES20.glUseProgram(renderProgram);

        if(baseObject!=ObjectBase.getEmpty()){
            Vector3 transform = baseObject.getPosition();
            Matrix.setIdentityM(model,0);
            Matrix.translateM(model,0,transform.x,transform.y,transform.z);
        }

        Matrix.multiplyMM(modelView, 0, view, 0, model, 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);

        // Set ModelView, MVP, position, normals, and color.
        GLES20.glUniform3fv(lightPosParam, 1, lightPosInEyeSpace, 0);
        GLES20.glUniformMatrix4fv(modelParam, 1, false, model, 0);
        GLES20.glUniformMatrix4fv(modelViewParam, 1, false, modelView, 0);
        GLES20.glUniformMatrix4fv(modelViewProjectionParam, 1, false, modelViewProjection, 0);
        GLES20.glVertexAttribPointer(
                positionParam, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(normalParam, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
        GLES20.glVertexAttribPointer(colorParam,4, GLES20.GL_FLOAT, false, 0, colorBuffer);


        GLES20.glEnableVertexAttribArray(positionParam);
        GLES20.glEnableVertexAttribArray(normalParam);
        GLES20.glEnableVertexAttribArray(colorParam);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, renderParamRows);

        RenderResources.checkGLError("drawing floor");

    }
}
