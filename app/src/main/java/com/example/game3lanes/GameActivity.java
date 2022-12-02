package com.example.game3lanes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    final int DELAY = 1000;
    private MaterialTextView game_LBL_score;
    private MaterialButton game_FAB_left;
    private MaterialButton game_FAB_right;
    private ShapeableImageView[] game_IMG_spaceships;
    private ShapeableImageView[] game_IMG_hearts;
    private ListView[] laneList;
    private ShapeableImageView game_IMG_alien;
    private gameManager manager;
    private LinearLayout ll;
    private long startTime = 0;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        findViews();

        manager = new gameManager();


        game_FAB_left.setOnClickListener(view -> {
            clicked(-1);
        });
        game_FAB_right.setOnClickListener(view -> {
            clicked(1);
        });

        startTime();
    }

    private void startTime() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateUI());
            }
        }, manager.DELAY, manager.DELAY);
                }

    private void updateUI() {
        manager.setScore(System.currentTimeMillis() - startTime);
        int seconds = (int) (manager.getScore() / manager.DELAY);
        //double points
        if(seconds>manager.BONUS)
            seconds *= 10;
        game_LBL_score.setText(String.format("%04d", seconds));
    }

    private void clicked(int dir) {
        if(dir>0)goRight();
        else goLeft();
    }

    private void findViews() {
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_FAB_left = findViewById(R.id.game_FAB_left);
        game_FAB_right = findViewById(R.id.game_FAB_right);
        game_IMG_spaceships = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_spaceship1),
                findViewById(R.id.game_IMG_spaceship2),
                findViewById(R.id.game_IMG_spaceship3),
        };
        game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
        };
        game_IMG_alien = findViewById(R.id.game_IMG_ufo1_1);
        ll = findViewById(R.id.game_LIST_lane1);
    };

    public void goRight() {
        if(manager.getSpaceshipLocation()!=manager.getMAXRIGHT()) {
            game_IMG_spaceships[manager.getSpaceshipLocation()].setVisibility(View.INVISIBLE);
            game_IMG_spaceships[manager.getSpaceshipLocation()+1].setVisibility(View.VISIBLE);
            manager.setSpaceship_location(1);
        }
    }

    public void goLeft() {
        if(manager.getSpaceshipLocation()!=manager.getMAXLEFT()) {
            game_IMG_spaceships[manager.getSpaceshipLocation()].setVisibility(View.INVISIBLE);
            game_IMG_spaceships[manager.getSpaceshipLocation()-1].setVisibility(View.VISIBLE);
            manager.setSpaceship_location(-1);
        }
    }

}