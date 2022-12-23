package com.example.game3lanes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private MaterialTextView game_LBL_score;
    private MaterialButton game_FAB_left;
    private MaterialButton game_FAB_right;
    private ShapeableImageView[] game_IMG_spaceships;
    private ShapeableImageView[] game_IMG_UFO;
    private ShapeableImageView[] game_IMG_hearts;
    private LinearLayout[] game_LANE_lane;
    private TextView game_ET_toast;
    private gameManager manager;
    private long startTime = 0;
    private Timer timer;
    private int gameType = 0;

    public static final String KEY_GAME_TYPE = "GAME_TYPE";



    ///Creating the app, creating a game manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent previousIntent = getIntent();
        int gameType = previousIntent.getIntExtra(KEY_GAME_TYPE, 0);
        findViews(gameType);
        manager = new gameManager();

        if(gameType == Main_Menu.BTN_MODE) {
            game_FAB_left.setOnClickListener(view -> {
                clicked(-1);
            });
            game_FAB_right.setOnClickListener(view -> {
                clicked(1);
            });
        }else
            gone_buttons();

        startTime();
        
        
    }



    ///Getting the length of the layout(Because OnCreate denies it) and creating runnable for the game

    private void startTime() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        ViewTreeObserver vto = game_LANE_lane[0].getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                game_LANE_lane[0].getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = game_LANE_lane[0].getMeasuredWidth();
                int height = game_LANE_lane[0].getMeasuredHeight();
                manager.setLaneLength(height);
                System.out.println("your lane length and width is" + width +"," + height);

            }
        });
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateUI());
            }
        }, manager.getDELAY(), manager.getDELAY());


    }

    ///Updating the UI each interval

    private void updateUI() {
        if(manager.getLaneLength()>0) {
            manager.setScore(((System.currentTimeMillis() - startTime)/ manager.getDELAY()) + manager.getBonusScore());
            game_LBL_score.setText(String.format("%06d", manager.getScore()));
            manager.createUFO();
            moveUFO();
            resetPos();
        }
    }

    /// move a UFO in each of the lanes if a UFO exists

    private void moveUFO() {
        for (int i =0 ; i<manager.getLANES();i++) {
            if (manager.getHasAsteroid(i)) {
                game_IMG_UFO[i].setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) game_LANE_lane[i].getLayoutParams();
                params.setMargins(params.leftMargin, manager.get_UFO_locationY(i) + manager.getMovementSpeed(i), params.rightMargin, params.bottomMargin);
                game_LANE_lane[i].setLayoutParams(params);
                manager.moveUFO();
            }
        }
    }

    ///If a UFO has reached the end of the lane reset his position. If a collision occured, activate the collision function for that lane.

    private void resetPos() {
        for (int i =0 ; i<manager.getLANES();i++) {
            if(manager.gotToEnd(i)) {
                if(manager.check_collision(i)){
                    collision(i);
                }else{
                    manager.setBonusScore(50);
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) game_LANE_lane[i].getLayoutParams();
                params.setMargins(params.leftMargin, 0, params.rightMargin, params.bottomMargin);
                manager.setUFO_locationY(i, -manager.get_UFO_locationY(i));
                manager.setHasUFO(i, false);
            }
        }
    }

    /* When a collision occurs:
    1. Decrease hearts by one
    2. Vibrate the phone
    3. Make a toast message
    4. Set bonus score negative 100
    5. make the UFO invisible
     */

    private void collision(int i){
        decrease_hearts();
        vibrate();
        toast();
        manager.setBonusScore(-100);
        invisibleUFO(i);
    }

    ///If a UFO has reached the end of the lane or collided with the spaceship make it invisible.

    private void invisibleUFO(int i){
        game_IMG_UFO[i].setVisibility(View.INVISIBLE);
    }


    

    ///Toast message for collision

    private void toast() {
        String name = String.valueOf(game_ET_toast.getText());
        Toast
                .makeText(this,"OUCH DODGE THE ALIENS PLEASE" + name,Toast.LENGTH_SHORT)
                .show();
    }

    ///Vibrate for collision

    private void vibrate() {

        Vibrator v = null;
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    ///Decreasing hearts for collision

    private void decrease_hearts() {
            if(manager.getLife() != 0) {
                game_IMG_hearts[manager.getLife() - 1].setVisibility(View.INVISIBLE);
                manager.setLife(manager.getLife() - 1);
            }
            if(manager.getLife() == 0) {
                manager.setLife(manager.getMAXLIFE());
                for (int i = 0; i < manager.getMAXLIFE(); i++) {
                    game_IMG_hearts[i].setVisibility(View.VISIBLE);
                }
            }

    }

    ///When clicking fab buttons

    private void clicked(int dir) {
        if(dir>0)goRight();
        else goLeft();
    }

    ///If clicking right and the spaceship is not at the far end - go right

    private void gone_buttons() {
        game_FAB_left.setVisibility(View.INVISIBLE);
        game_FAB_right.setVisibility(View.INVISIBLE);
    }

    public void goRight() {
        if(manager.getSpaceshipLocation()!=manager.getMAXRIGHT()) {
            game_IMG_spaceships[manager.getSpaceshipLocation()].setVisibility(View.INVISIBLE);
            game_IMG_spaceships[manager.getSpaceshipLocation()+1].setVisibility(View.VISIBLE);
            manager.setSpaceship_location(1);
        }
    }

    ///If clicking left and the spaceship is not at the far end - go left

    public void goLeft() {
        if(manager.getSpaceshipLocation()!=manager.getMAXLEFT()) {
            game_IMG_spaceships[manager.getSpaceshipLocation()].setVisibility(View.INVISIBLE);
            game_IMG_spaceships[manager.getSpaceshipLocation()-1].setVisibility(View.VISIBLE);
            manager.setSpaceship_location(-1);
        }
    }

    ///Set up views

    private void findViews(int gameType) {
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
        game_IMG_UFO = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_ufo1),
                findViewById(R.id.game_IMG_ufo2),
                findViewById(R.id.game_IMG_ufo3),
        };
        game_LANE_lane = new LinearLayout[]{
                findViewById(R.id.game_LIST_lane1),
                findViewById(R.id.game_LIST_lane2),
                findViewById(R.id.game_LIST_lane3)
        };
        game_ET_toast = findViewById(R.id.game_ET_toast);
    }



}