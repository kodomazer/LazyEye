package zhaos.pong;

import java.nio.FloatBuffer;

/**
 * Created by kodomazer on 9/3/2016.
 */
public interface renderableObject {

    class Point{
        public float x;
        public float y;
        public float z;

        public Point(){
            x=0;
            y=0;
            z=0;
        }
        public Point(int x){
            this.x=x;
            this.y=0;
            this.z=0;
        }
        public Point(int x,int y){
            this.x=x;
            this.y=y;
            this.z=0;
        }
        public Point(int x,int y,int z){
            this.x=x;
            this.y=y;
            this.z=z;
        }
    }

    Point getPosition();
    FloatBuffer getCoords();
    FloatBuffer getColors();
    FloatBuffer getNormals();
}

