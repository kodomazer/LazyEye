package zhaos.pong;

import android.os.Bundle;

import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.egl.EGLConfig;


public class MainView extends GvrActivity implements GvrView.StereoRenderer {

    private RenderItems item;
    private static final String TAG = "MainView";


    // We keep the light always position just above the user.
    private static final float[] LIGHT_POS_IN_WORLD_SPACE = new float[] {0.0f, 2.0f, 0.0f, 1.0f};

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 100.0f;

    private static final float CAMERA_Z = 0.01f;

    private final float[] lightPosInEyeSpace = new float[4];

    private float[] headRotation;

    private float[] camera;
    private float[] view;
    private float[] headView;
    private float[] modelViewProjection;
    private float[] modelView;
    private float[] modelFloor;

    private Plane wall;

    @Override
    public void onPause() {
//        gvrAudioEngine.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
//        gvrAudioEngine.resume();
    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }
    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }


    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader.
     *
     * @param type The type of shader we will be creating.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The shader object handler.
     */
    public int loadGLShader(int type, int resId) {
        String code = readRawTextFile(resId);
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    /**
     * Converts a raw text file into a string.
     *
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The context of the text file, or null in case of error.
     */
    private String readRawTextFile(int resId) {
        InputStream inputStream = getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     *
     * @param label Label to report in case of error.
     */
    private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }

    /**
     * Sets the view to our GvrView and initializes the transformation matrices we will use
     * to render our scene.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        item = RenderItems.getInstance();
        item.setView(this);


        super.onCreate(savedInstanceState);

        initializeGvrView();

//        modelCube = new float[16];
        camera = new float[16];
        view = new float[16];
        modelViewProjection = new float[16];
        modelView = new float[16];
        modelFloor = new float[16];
//        tempPosition = new float[4];
        // Model first appears directly in front of user.
//        modelPosition = new float[] {0.0f, 0.0f, -MAX_MODEL_DISTANCE / 2.0f};
        headRotation = new float[4];
        headView = new float[16];
        wall = new Plane();
    }

    public void initializeGvrView() {
        setContentView(R.layout.common_ui);

        GvrView gvrView = (GvrView) findViewById(R.id.gvr_view);
        gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);

        gvrView.setRenderer(this);
        gvrView.setTransitionViewEnabled(true);
        gvrView.setOnCardboardBackButtonListener(
                new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                });

        if (gvrView.setAsyncReprojectionEnabled(true)) {
            // Async reprojection decouples the app framerate from the display framerate,
            // allowing immersive interaction even at the throttled clockrates set by
            // sustained performance mode.
            AndroidCompat.setSustainedPerformanceMode(this, true);
        }

        setGvrView(gvrView);
    }

    /**
     * Creates the buffers we use to store information about the 3D world.
     *
     * <p>OpenGL doesn't use Java arrays, but rather needs data in a format it can understand.
     * Hence we use ByteBuffers.
     *
     * Everything that needs GLES20 needs to be called in or after here
     *
     *
     * @param config The EGL configuration used when creating the surface.
     */
    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well.
        item.GLESLoaded();

        wall.initializePlane();
        Matrix.setIdentityM(modelFloor, 0);
        Matrix.translateM(modelFloor, 0, 0, -20, 0);

    }


    @Override
    public void onFinishFrame(Viewport viewport) {}


    public void onDrawEye(Eye eye) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        checkGLError("colorParam");

        // Apply the eye transformation to the camera.
        Matrix.multiplyMM(view, 0, eye.getEyeView(), 0, camera, 0);

        // Set the position of the light
        Matrix.multiplyMV(lightPosInEyeSpace, 0, view, 0, LIGHT_POS_IN_WORLD_SPACE, 0);

        // Build the ModelView and ModelViewProjection matrices
        // for calculating cube position and light.
        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);

        // Set modelView for the floor, so we draw floor in the correct location
        Matrix.multiplyMM(modelView, 0, view, 0, modelFloor, 0);
        Matrix.multiplyMM(modelViewProjection, 0, perspective, 0, modelView, 0);

        wall.Draw(modelFloor, modelView, modelViewProjection, lightPosInEyeSpace);
        //drawFloor();
    }

    /**
     * Prepares OpenGL ES before we draw a frame.
     *
     * @param headTransform The head transformation in the new frame.
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {

        // Build the camera matrix and apply it to the ModelView.
        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        headTransform.getHeadView(headView, 0);

        headTransform.getQuaternion(headRotation, 0);


        checkGLError("onReadyToDraw");
    }

}
final class WorldLayoutData {

    public static final float[] CUBE_COORDS = new float[] {
            // Front face
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,

            // Right face
            1.0f, 1.0f, 1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, 1.0f, -1.0f,

            // Back face
            1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, -1.0f,

            // Left face
            -1.0f, 1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, 1.0f, 1.0f,

            // Top face
            -1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,
            -1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, -1.0f,

            // Bottom face
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, 1.0f,
            -1.0f, -1.0f, -1.0f,
    };

    public static final float[] CUBE_COLORS = new float[] {
            // front, green
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,

            // right, blue
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,

            // back, also green
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,
            0f, 0.5273f, 0.2656f, 1.0f,

            // left, also blue
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,

            // top, red
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,

            // bottom, also red
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
            0.8359375f,  0.17578125f,  0.125f, 1.0f,
    };

    public static final float[] CUBE_FOUND_COLORS = new float[] {
            // front, yellow
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,

            // right, yellow
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,

            // back, yellow
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,

            // left, yellow
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,

            // top, yellow
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,

            // bottom, yellow
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
            1.0f,  0.6523f, 0.0f, 1.0f,
    };

    public static final float[] CUBE_NORMALS = new float[] {
            // Front face
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,

            // Right face
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,

            // Back face
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,

            // Left face
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,

            // Top face
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,

            // Bottom face
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f
    };

    // The grid lines on the floor are rendered procedurally and large polygons cause floating point
    // precision problems on some architectures. So we split the floor into 4 quadrants.
    public static final float[] FLOOR_COORDS = new float[] {
            // +X, +Z quadrant
            7, 10, 7,
            10, 30, 5,
            5, 30, 10,
            100, 100, 0,
            0, 100, 100,
            100, 200, 100,

            // -X, +Z quadrant
            0, 0, 0,
            -200, 0, 0,
            -200, 0, 200,
            0, 0, 0,
            -200, 0, 200,
            0, 0, 200,

            // +X, -Z quadrant
            200, 0, -200,
            0, 0, -200,
            0, 0, 0,
            200, 0, -200,
            0, 0, 0,
            200, 0, 0,

            // -X, -Z quadrant
            0, 0, -200,
            -200, 0, -200,
            -200, 0, 0,
            0, 0, -200,
            -200, 0, 0,
            0, 0, 0,
    };

    public static final float[] FLOOR_NORMALS = new float[] {
            0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
    };

    public static final float[] FLOOR_COLORS = new float[] {
            1.0f,  0.0f,  0.0f, 1.0f,
            0.0f,  1.0f,  0.0f, 1.0f,
            0.0f,  0.0f,  1.0f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
            0.0f, 0.3398f, 0.9023f, 1.0f,
    };

    public static final float[] FLOOR_COLORS1 = new float[] {
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
            1.0f, 0.3398f, 0.9023f, 1.0f,
    };
}