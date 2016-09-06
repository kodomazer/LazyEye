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

    public abstract boolean collidesWith(Vector3 position, Vector3 velocity);
    public abstract void collisionResult(Vector3 position, Vector3 velocity);


}
