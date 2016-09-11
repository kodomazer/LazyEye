package zhaos.pong;

import static zhaos.pong.Pong.getGame;

/**
 * Created by ambersz on 9/5/2016.
 */
public class Goal extends Blocks{
    private Vector3 score;


    public Goal(ObjectBase parent, float yCoord, Vector3 goalScore) {
        super(parent);
        transform = new Vector3(0,yCoord);
        score = goalScore;

    }

    public boolean collidesWith(Ball b, Vector3 delta){
        float direction;
        if (score.y>0){
            direction = -1;
        } else {
            direction = 1;
        }
        if(direction * (b.transform.y + delta.y - transform.y) > 0){
            getGame().updateScore(score);
            b.kill();
            return true;
        } else{
            return false;
        }
    }


}
