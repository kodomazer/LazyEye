package zhaos.pong;

/**
 * Created by kodomazer on 9/4/2016.
 * Base classes for any objects that might contain a renderable object
 */
public class ObjectBase {
    protected ObjectBase parent;
    protected RenderingBase render;

    protected Vector3 transform;

    static public final ObjectBase empty = new ObjectBase(null);

    public ObjectBase(ObjectBase parent){
        transform=new Vector3();
        this.parent = parent;
        render = null;
    }

    static ObjectBase getEmpty(){
        return empty;
    }


    public RenderingBase getRender(){
        return render;
    }

    public Vector3 getPosition(){
        if(parent==null)
            return transform;
        return transform.add(parent.getPosition());
    }

    public void setPosition(Vector3 newPosition){
        transform.x = newPosition.x;
        transform.y = newPosition.y;
        transform.z = newPosition.z;
    }

    public void translate(Vector3 movement){
        transform.x += movement.x;
        transform.y += movement.y;
        transform.z += movement.z;
    }




}
