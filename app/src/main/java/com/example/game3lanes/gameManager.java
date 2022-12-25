package com.example.game3lanes;


public class gameManager {
    private final int LANES = 5;
    private final int MAXRIGHT = LANES - 1;
    private final int MAXLEFT = 0;
    private final int SHIPHEIGHT = 50;
    private final int BONUS = 10;
    private final int MINVOL = 8;
    private final int DELAY = 50;
    private final int MAXLIFE = 3;
    private final int MOV_SPEED = 20;

    private int life = 3;
    private long score = 0;
    private int bonusScore = 0;

    private int spaceship_location = 2;
    private int[] UFO_locationY;
    private int laneLength;


    private boolean[] hasUFO;

    public gameManager(){
        UFO_locationY = new int[LANES];
        hasUFO = new boolean[]{
                false,
                false,
                false,
                false,
                false
        };
    }

    public void createUFO(){
        for (int i=0; i<LANES; i++){
            if(!getHasUFO(i)) {
                int num = (int) (((Math.random()) * 100) + 1);
                if (num > 90) {
                    setHasUFO(i, true);
                }
            }
        }

    }

    public void moveUFO(){
        for (int i = 0; i<LANES ;i++){
            if(getHasUFO(i) && UFO_locationY[i] <= laneLength)
                UFO_locationY[i] += MOV_SPEED;
        }
    }

    public boolean check_collision(int i) {
            if(getHasUFO(i)) {
                return (gotToEnd(i)) && (getUFOlocationX(i) == getSpaceshipLocation());
            }
        return false;
    }

    public boolean gotToEnd(int i){
        if(getHasUFO(i)) {
            return get_UFO_locationY(i) >= getLaneLength();
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

    public int getDELAY() {
        return DELAY;
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

    public int getUFOlocationX(int i) {
        return i;
    }

    public int get_UFO_locationY(int i) {
        return UFO_locationY[i];
    }

    public void setUFO_locationY(int i, int adder) {
        this.UFO_locationY[i] += adder;
    }

    public int getLaneLength() {
        return laneLength;
    }

    public void setLaneLength(int laneLength) {
        this.laneLength = laneLength;
    }

    public boolean getHasUFO(int i) {
        return hasUFO[i];
    }

    public void setHasUFO(int i, boolean bool) {
        hasUFO[i] = bool;
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
