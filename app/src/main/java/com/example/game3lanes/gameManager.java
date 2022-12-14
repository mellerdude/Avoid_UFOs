package com.example.game3lanes;


public class gameManager {
    private final int LANES = 5;
    private final int MAXRIGHT = LANES - 1;
    private final int MAXLEFT = 0;
    private final int SHIPHEIGHT = 50;
    private final int BONUS = 10;
    private final int MINVOL = 8;
    private final int MOV_SPEED = 15;
    public final static int FAST_DELAY = 50;
    public final static int SLOW_DELAY = 100;
    private final int MAXLIFE = 3;

    private int delay;
    public final int OBS_TYPE_UFO = 0;
    public final int OBS_TYPE_BURGER = 1;
    private int life = 3;
    private long score = 0;
    private int bonusScore = 0;

    private int spaceship_location = 2;
    private int[] obstacleLocation_y;
    private int laneLength;


    private boolean[] hasObstacle;
    private int[] obstacleType;

    public gameManager(int gameType){
        obstacleLocation_y = new int[LANES];
        hasObstacle = new boolean[LANES];
        obstacleType = new int[LANES];


        if(gameType == Main_Menu.BTN_MODE_SLOW || gameType == Main_Menu.SNSR_MODE) {
            setDelay(SLOW_DELAY);
        }
        else {
            setDelay(FAST_DELAY);
        }
    }

    public void createObstacle(){
        for (int i=0; i<LANES; i++){
            if(!getHasObstacle(i)) {
                int num = (int) (((Math.random()) * 100) + 1);
                if (num > 40) {
                    setHasObstacle(i, true);
                    if(num%3 == 0)
                        setObstacleType(i, OBS_TYPE_BURGER);
                    else
                        setObstacleType(i, OBS_TYPE_UFO);

                }
            }
        }

    }

    public void moveObstacle(){
        for (int i = 0; i<LANES ;i++){
            if(getHasObstacle(i) && obstacleLocation_y[i] <= laneLength)
                obstacleLocation_y[i] += getMovementSpeed();
        }
    }

    public boolean check_collision(int i) {
            if(getHasObstacle(i) && getObstacleType(i) == OBS_TYPE_UFO) {
                return (getObstaclelocationX(i) == getSpaceshipLocation());
            }
        return false;
    }

    public boolean check_eating(int i) {
        if(getHasObstacle(i) && getObstacleType(i) == OBS_TYPE_BURGER) {
            return (getObstaclelocationX(i) == getSpaceshipLocation());
        }
        return false;
    }

    public boolean gotToEnd(int i){
        if(getHasObstacle(i)) {
            return get_obstacle_locationY(i) >= getLaneLength();
        }
        return false;
    }


    public void setBonusScore(int replacer){
        if(replacer<0 && (bonusScore + replacer) < 0)
            this.bonusScore = 0;
        else
            bonusScore = bonusScore + replacer;
    }


    public int getMovementSpeed(){
        return MOV_SPEED;
    }


    public long getScore() {
        return score;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDELAY() {
        return delay;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public void addScore(long score) {
        this.score += score;
    }
    public int getSpaceshipLocation() {
        return spaceship_location;
    }

    public void setSpaceship_location(int adder) {
        spaceship_location += adder;
    }


    public int getMAXRIGHT() {
        return MAXRIGHT;
    }

    public int getMAXLEFT() {
        return MAXLEFT;
    }

    public int getLANES() {
        return LANES;
    }

    public int getObstaclelocationX(int i) {
        return i;
    }

    public int get_obstacle_locationY(int i) {
        return obstacleLocation_y[i];
    }

    public void setObstacle_location_y(int i, int adder) {
        this.obstacleLocation_y[i] += adder;
    }

    public int getObstacleType(int i) {
        return obstacleType[i];
    }

    public void setObstacleType(int i, int type) {
        this.obstacleType[i] = type;
    }

    public int getLaneLength() {
        return laneLength;
    }

    public void setLaneLength(int laneLength) {
        this.laneLength = laneLength;
    }

    public boolean getHasObstacle(int i) {
        return hasObstacle[i];
    }

    public void setHasObstacle(int i, boolean bool) {
        hasObstacle[i] = bool;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMAXLIFE() {
        return MAXLIFE;
    }

    public int getBonusScore(){
        return bonusScore;
    }


    public int getSHIPHEIGHT() {
        return SHIPHEIGHT;
    }
}
