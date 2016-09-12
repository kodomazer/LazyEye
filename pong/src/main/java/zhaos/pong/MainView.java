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


import javax.microedition.khronos.egl.EGLConfig;


public class MainView extends GvrActivity implements GvrView.StereoRenderer {

    private RenderResources item;
    private static final String TAG = "MainView";


    // We keep the light always position just above the user.
    private static final float[] LIGHT_POS_IN_WORLD_SPACE = new float[]
            {0.0f, 2.0f, 0.0f, 1.0f};

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 100.0f;

    private static final float CAMERA_Z = 0.01f;

    private final float[] lightPosInEyeSpace = new float[4];

    private float[] headRotation;

    private float[] camera;
    private float[] view;
    private float[] headView;

    private Floor wall;

    protected long lastFrame;
    protected Pong gameInstance;

    protected RenderingBase[] renderList;


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
       RenderResources.getInstance().GLESUnloaded();
    }
    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }


    /**
     * Sets the view to our GvrView and initializes the transformation matrices we will use
     * to render our scene.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        item = RenderResources.getInstance();
        item.setView(this);

        lastFrame = System.currentTimeMillis();
        super.onCreate(savedInstanceState);

        initializeGvrView();

        camera = new float[16];
        view = new float[16];

        headRotation = new float[4];
        headView = new float[16];
        wall = new Floor();
        gameInstance = new Pong();
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

        wall.BuildModel();
    }


    @Override
    public void onFinishFrame(Viewport viewport) {}


    public void onDrawEye(Eye eye) {
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);



        // Apply the eye transformation to the camera.
        Matrix.multiplyMM(view, 0, eye.getEyeView(), 0, camera, 0);

        // Set the position of the light
        Matrix.multiplyMV(lightPosInEyeSpace, 0, view, 0, LIGHT_POS_IN_WORLD_SPACE, 0);

        // Build the ModelView and ModelViewProjection matrices
        // for calculating cube position and light.
        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);

        wall.Draw(view, perspective, lightPosInEyeSpace);

        gameInstance.getRender().Draw(view, perspective, lightPosInEyeSpace);

        //Draw respective objects in each eye
        for (ObjectBase o : eye.getType() == 1 ?
                gameInstance.getRenderListLeft() :
                gameInstance.getRenderListRight()) {
            RenderingBase r = o.getRender();
            if (r!=null)
                r.Draw(view, perspective, lightPosInEyeSpace);
        }
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
        //deltaT: float
        //LookAngle: Vector3
        float[] forward = new float[10];
        headTransform.getForwardVector(forward,0);

        Vector3 lookAt = new Vector3();
        lookAt.x = 10.0f*(float)Math.tan(forward[0]);
        long thisFrameTime = System.currentTimeMillis();
        gameInstance.tick((float)(thisFrameTime-lastFrame)/1000.0f,lookAt);
        lastFrame = thisFrameTime;

    }

    @Override
    public void onCardboardTrigger() {
        super.onCardboardTrigger();
    }
}