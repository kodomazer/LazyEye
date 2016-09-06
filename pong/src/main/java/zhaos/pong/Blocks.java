package zhaos.pong;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by ambersz on 9/4/2016.
 */
public abstract class Blocks extends ObjectBase {

    public Blocks(ObjectBase parent){
        super(parent);
    }

    public abstract boolean collidesWith(Ball ball, Vector3 velocity);


}
