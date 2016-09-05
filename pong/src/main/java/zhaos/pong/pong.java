package zhaos.pong;


/**
 * Created by kodomazer on 9/2/2016.
 */
public class Pong{

    protected Vector3 transform;
    private Ball puck;
    private Paddle playerPaddle;
    private Paddle opponentPaddle;

    public Pong(){
        puck = new Ball();
        playerPaddle = new Paddle();
        opponentPaddle = new Paddle();

    }

    public Vector3 getTransform(){
        return transform;
    }
    public Ball getPuck(){
        return puck;
    }
    public Paddle getPlayer(){
        return playerPaddle;
    }
    public Paddle getOpponent(){
        return opponentPaddle;
    }

}
