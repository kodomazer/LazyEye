package zhaos.pong;


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
    private int score;
    private ObjectBase[] renderLeft;
    private ObjectBase[] renderRight;
    private Goal playerGoal;
    private Goal opponentGoal;
    private float gameHeight;
    private float gameWidth;

    public Pong(){
        super(null);
        game = this;
        gameHeight = 15;//setting play area height and width
        gameWidth = 10;
        puck = new Ball[1];
        puck[0] = new Ball(this);
        playerPaddle = new Paddle(this,-gameHeight, 5);
        opponentPaddle = new Paddle(this, gameHeight, 5);
        playerGoal = new Goal(this,-gameHeight-5,-1);//goals 5 units beyond game boundaries
        opponentGoal = new Goal(this,gameHeight+5, 1);
        leftWall = new Wall(this,-gameWidth);
        rightWall = new Wall(this,gameWidth);

//        gameOver = false;
        score = 0;
        colliders = new Blocks[6];

        //Collide with paddles, goals, walls in order

        colliders[0] = playerPaddle;
        colliders[1] = opponentPaddle;
        colliders[2] = playerGoal;
        colliders[3] = opponentGoal;
        colliders[4] = leftWall;
        colliders[5] = rightWall;

        //

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
                new Vector3(-gameWidth,gameHeight,-0.1f),
                new Vector3(gameWidth,gameHeight,-0.1f),
                new Vector3(gameWidth,-gameHeight,-0.1f),
                new Vector3(-gameWidth,-gameHeight,-0.1f));
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
                collided = c.collidesWith(b, b.getVelocity().scale(deltaT));
            }

        }
        return collided;
        /** 9/5/2016 check for each element of colliders if collision;
         * return true if it collided with anything
         * each collider should change the relevant game state
         * (walls & paddles change vel; goals kill ball)
         */
    }

    public void tick(float deltaT, Vector3 playerSight) {

        if (getNumPucks() > 0) {
            playerPaddle.updatePaddlePosition(playerSight.x); //update paddle positions
            opponentPaddle.updatePaddlePosition(paddleAI(opponentPaddle,deltaT)); //update opponent paddle

            //move balls
            for (Ball b : puck) {
                if (checkCollisions(b, deltaT)) {
                    break;
                } else {
                    b.move(deltaT);
                }
            }
        }else if(score>0) {
                //TODO GAME OVER; YOU WIN
        } else if(score < 0){
                //TODO GAME OVER; YOU LOSE
        } else {// SCORE IS 0 AND SOMETHING WENT WRONG

        }
    }

    private float paddleAI(Paddle controlledPaddle, float deltaT){
        Ball closestApproaching = getPuck(1);
        for (Ball b: puck) {
            if (b.getVelocity().y > 0 && b.getPosition().y > closestApproaching.getPosition().y) {
                closestApproaching = b;
            }
        }
        float noisyDistance = closestApproaching.transform.x - controlledPaddle.transform.x + (float) Math.random()/2 *deltaT ;
        return controlledPaddle.getPosition().x+ Math.copySign(Math.min(Math.abs(noisyDistance),deltaT*10*10/16),noisyDistance);
    }

    public void updateScore(int i){
        score +=i;
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

    public float getGameWidth(){
        return gameWidth;
    }

    public float getGameHeight(){
        return gameHeight;
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
