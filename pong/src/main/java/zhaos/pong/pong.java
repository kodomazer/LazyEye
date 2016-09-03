package zhaos.pong;


/**
 * Created by kodomazer on 9/2/2016.
 */
public class Pong  {

    private Ball puck;
    private Paddle playerPaddle;
    private Paddle opponentPaddle;

    public Pong(){
        puck = new Ball();
        playerPaddle = new Paddle();
        opponentPaddle = new Paddle();

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
