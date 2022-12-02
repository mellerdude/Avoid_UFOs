package com.example.game3lanes;


import android.content.Context;

import com.google.android.material.imageview.ShapeableImageView;

public class gameManager {
    private long score = 0;
    private int spaceship_location = 1;
    private int obstacle_X;
    private int obstacle_Y;
    public final int MAXRIGHT = 2;
    public final int MAXLEFT = 0;
    public final int BONUS = 10;
    public final int DELAY = 1000;

    public static boolean check_collision() {
        return false;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getSpaceshipLocation() {
        return spaceship_location;
    }

    public void setSpaceship_location(int adder) {
        spaceship_location += adder;
    }

    public int getObstacle_X() {
        return obstacle_X;
    }

    public void setObstacle_X(int obstacle_X) {
        this.obstacle_X = obstacle_X;
    }

    public int getObstacle_Y() {
        return obstacle_Y;
    }

    public void setObstacle_Y(int obstacle_Y) {
        this.obstacle_Y = obstacle_Y;
    }

    public int getMAXRIGHT() {
        return MAXRIGHT;
    }

    public int getMAXLEFT() {
        return MAXLEFT;
    }


}
