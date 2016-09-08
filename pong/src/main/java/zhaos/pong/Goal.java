package zhaos.pong;

import static zhaos.pong.Pong.getGame;

/**
 * Created by ambersz on 9/5/2016.
 */
public class Goal extends Blocks{
    private int score;


    public Goal(ObjectBase parent, float yCoord, int goalScore) {
        super(parent);
        transform = new Vector3(0,yCoord);
        score = goalScore;

    }

    public boolean collidesWith(Ball b, Vector3 delta){
        if((b.transform.y - transform.y) * (b.transform.y + delta.y - transform.y) < 0){
            getGame().updateScore(score);
            b.kill();
            return true;
        } else{
            return false;
        }
    }


}
