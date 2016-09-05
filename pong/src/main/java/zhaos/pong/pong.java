package zhaos.pong;


/**
 * Created by kodomazer on 9/2/2016.
 */
public class Pong{

    protected Vector3 transform;
    private Ball[] puck;
    private Paddle playerPaddle;
    private Paddle opponentPaddle;
    private Wall leftWall;
    private Wall rightWall;
    private Blocks[] colliders;
    private boolean gameOver;
    private int score;
    private renderableObject[] renderLeft;
    private renderableObject[] renderRight;

    public Pong(){
        puck = new Ball[1];
        puck[0] = new Ball(this);
        playerPaddle = new Paddle();
        opponentPaddle = new Paddle();
        leftWall = new Wall(-10);
        rightWall = new Wall(10);
        gameOver = false;
        score = 0;
        colliders = new Blocks[4];

        renderLeft = new renderableObject[4]; //needs to be expanded if more than 1 ball
        renderRight = new renderableObject[4]; //needs to be expanded if more than 1 ball

        //render ball on left and paddle on right

        renderLeft[0] = renderRight[0] = leftWall;
        renderLeft[1] = renderRight[1] = rightWall;
        renderLeft[2] = renderRight[2] = opponentPaddle;
        renderRight[3] = playerPaddle;
        renderLeft[3] = getPuck(1); // ONLY THE FIRST BALL WILL BE RENDERED IF INCLUDING MORE THAN ONE
    }
    
    private void tick(float deltaT){
        while (getNumPucks()>0){
            //// TODO: 9/4/2016 update paddle location
            for (Ball b : puck) {//move balls
                b.tick();
                if(b.isDead()){
                    gameOver = true;
                    break
                }
            }
        }
        if (puck.length == 0)
    }

    public Vector3 getTransform(){
        return transform;
    }

    public Ball getPuck(int i){
        return puck[i-1];
    }
    public int getNumPucks(){return puck.length;}

    public Paddle getPlayer(){
        return playerPaddle;
    }
    public Paddle getOpponent(){
        return opponentPaddle;
    }
    public renderableObject[] getRenderListLeft(){
        return renderLeft;
    }

    public renderableObject[] getRenderListRight(){
        return renderRight;
    }
}
