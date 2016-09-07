package zhaos.pong;

/**
 * Created by kodomazer on 9/4/2016.
 */
public class Vector3 {
    public float x;
    public float y;
    public float z;

    public Vector3(){
        x=0;
        y=0;
        z=0;
    }
    public Vector3(float x){
        this.x=x;
        this.y=0;
        this.z=0;
    }
    public Vector3(float x,float y){
        this.x=x;
        this.y=y;
        this.z=0;
    }
    public Vector3(float x,float y,float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public Vector3(Vector3 a) {
        x = a.x;
        y = a.y;
        z = a.z;
    }
    public Vector3 add(Vector3 a){
        return new Vector3(this.x+a.x,
                this.y+a.y,
                this.z+a.z);
    }

    public Vector3 subtract(Vector3 a){
        return new Vector3(this.x-a.x,
                this.y-a.y,
                this.z-a.z);
    }

    public Vector3 scale(float a){return new Vector3(this.x*a, this.y*a, this.z*a);}

    public Vector3 cross(Vector3 a) {
        return new Vector3(
                this.y * a.z - this.z * a.y,
                this.z * a.x - this.x * a.z,
                this.x * a.y - this.y * a.x);
    }
    public float dot(Vector3 a) {
        return this.x * a.x + this.y * a.y + this.z * a.z;
    }

   public float norm(){
       return (float) Math.sqrt( x*x + y*y+z*z);
   }

    public Vector3 reflectOver(Vector3 b){//Assumes both vectors are in 2d xy plane; ignores z-coordinate.
        float magnitude = b.norm();
        Vector3 a = new Vector3(b.y/magnitude,b.x/magnitude);
        return this.add(a.scale(this.dot(a)*-2));

    }

    public Vector3 reflectOverNorm(Vector3 a){//Assumes a has magnitude 1

        return this.add(a.scale(this.dot(a)*-2));

    }

}
