package zhaos.pong;


import java.util.function.ToDoubleBiFunction;

/**
 * Created by kodomazer on 9/2/2016.
 */



public class Pong extends ObjectBase{

    static private Pong game;
    static public Pong getGame() {
        return game;
    }
    protected Vector3 transform;
    private Ball[] puck;
    private Paddle playerPaddle;
    private Paddle opponentPaddle;
    private Wall leftWall;
    private Wall rightWall;
    private Blocks[] colliders;
    private boolean gameOver;
    private int score;
    private ObjectBase[] renderLeft;
    private ObjectBase[] renderRight;

    public Pong(){
        super(null);
        puck = new Ball[1];
        puck[0] = new Ball(this);
        playerPaddle = new Paddle(this);
        opponentPaddle = new Paddle(this);
        leftWall = new Wall(this,-10);
        rightWall = new Wall(this,10);
        gameOver = false;
        score = 0;
        colliders = new Blocks[4];

        renderLeft = new ObjectBase[4]; //needs to be expanded if more than 1 ball
        renderRight = new ObjectBase[4]; //needs to be expanded if more than 1 ball

        //render ball on left and paddle on right, all else rendered for both eyes

        renderLeft[0] = renderRight[0] = leftWall;
        renderLeft[1] = renderRight[1] = rightWall;
        renderLeft[2] = renderRight[2] = opponentPaddle;
        renderRight[3] = playerPaddle;
        renderLeft[3] = getPuck(1);
        // ONLY THE FIRST BALL WILL BE RENDERED IF INCLUDING MORE THAN ONE

        Quad background = new Quad(this);
        background.setVertices(
                new Vector3(-10,20,-1),
                new Vector3(10,20,-1),
                new Vector3(10,-20,-1),
                new Vector3(-10,-20,-1));
        render = background;
        render.setUniformColor(new float[]{0.5f,0.5f,0.5f,0.5f});
        translate(new Vector3(0,0,-20));

    }

    private boolean checkCollisions(Ball b, float deltaT) {
        boolean collided = false;
        for (Blocks c : colliders) {
            if (collided) {
                break;
            } else {
                collided = c.collidesWith(b.getPosition(), b.getVelocity().scale(deltaT));
            }

        }
        return collided;
        /** TODO: 9/5/2016 check for each element of colliders if collision;
         * return true if it collided with anything
         * each collider should change the relevant game state
         * (walls & paddles change vel; goals kill ball)
         */
    }

    private void tick(float deltaT) {
        if (getNumPucks() > 0) {
            //// TODO: 9/4/2016 update paddle location

            //move balls
            for (Ball b : puck) {
                if (checkCollisions(b, deltaT)) {
                    break;
                } else {
                    b.move(deltaT);
                }
            }
        }

        if(score>0) {
                //TODO GAME OVER; YOU WIN
        } else if(score < 0){
                //TODO GAME OVER; YOU LOSE
        } else {// SCORE IS 0 AND SOMETHING WENT WRONG

        }
    }

    public Ball getPuck(int i){
        return puck[i-1];
    } // Get puck, where first puck is returned from getPuck(1)

    public int getNumPucks(){//returns number of active balls
        int num = 0;
        for(Ball b:puck){
            if(b.isActive()){
                num+=1;
            }
        }
        return num;
    }

    public Paddle getPlayer(){
        return playerPaddle;
    }
    public Paddle getOpponent(){
        return opponentPaddle;
    }
    public ObjectBase[] getRenderListLeft(){
        return renderLeft;
    }

    public ObjectBase[] getRenderListRight(){
        return renderRight;
    }
}
