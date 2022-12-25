package com.example.game3lanes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.game3lanes.interfaces.TiltCallback;
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
    private ShapeableImageView[] game_IMG_Burger;
    private ShapeableImageView[] game_IMG_hearts;
    private LinearLayout[] game_LANE_lane;
    private TextView game_ET_toast;
    private gameManager manager;
    private long startTime = 0;
    private Timer timer;
    private int gameType = 0;
    Context context;
    public static final String KEY_GAME_TYPE = "GAME_TYPE";
    private Tilt_Detector tilt_detector;

    ///Creating the app, creating a game manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context =  this.getApplicationContext();
        setContentView(R.layout.activity_game);
        Intent previousIntent = getIntent();
        this.gameType = previousIntent.getIntExtra(KEY_GAME_TYPE, 0);
        findViews(gameType);

        if(gameType == Main_Menu.BTN_MODE_FAST || gameType == Main_Menu.BTN_MODE_SLOW) {
            game_FAB_left.setOnClickListener(view -> {
                clicked(-1);
            });
            game_FAB_right.setOnClickListener(view -> {
                clicked(1);
            });
        }else
            invisible_buttons();

        manager = new gameManager(gameType);
        if(gameType == Main_Menu.SNSR_MODE)
            initTiltDetector();
        startTime();
        
        
    }

    private void initTiltDetector() {
        tilt_detector = new Tilt_Detector(this, new TiltCallback() {


            @Override
            public void stepXPlusOne() {
                goRight();
            }



            @Override
            public void stepXNegativeOne() {
                goLeft();
            }



            @Override
            public void stepYPos() {
                manager.setMovementSpeed(manager.getMovementSpeed() + 5);
            }
            @Override
            public void stepYNeg() {
                manager.setMovementSpeed(manager.getMovementSpeed() - 5);
            }
        });
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
                manager.setLaneLength(height-manager.getSHIPHEIGHT());
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
            manager.createObstacle();
            moveUFO();
            resetPos();
        }
    }

    /// move a UFO in each of the lanes if a UFO exists

    private void moveUFO() {
        for (int i =0 ; i<manager.getLANES();i++) {
            if (manager.getHasObstacle(i)) {
                manager.moveObstacle();
                if(manager.getObstacleType(i) == manager.OBS_TYPE_UFO)
                    game_IMG_UFO[i].setVisibility(View.VISIBLE);
                else
                    game_IMG_Burger[i].setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) game_LANE_lane[i].getLayoutParams();
                params.setMargins(params.leftMargin, manager.get_obstacle_locationY(i), params.rightMargin, params.bottomMargin);
                game_LANE_lane[i].setLayoutParams(params);
            }
        }
    }

    ///If a UFO has reached the end of the lane reset his position. If a collision occured, activate the collision function for that lane.

    private void resetPos() {
        for (int i =0 ; i<manager.getLANES();i++) {
            if(manager.gotToEnd(i)) {
                goneObstacle(i);
                if(manager.check_collision(i)){
                    collision(i);
                }
                if(manager.check_eating(i)){
                    manager.setBonusScore(1000);
                }
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) game_LANE_lane[i].getLayoutParams();
                params.setMargins(params.leftMargin, 0, params.rightMargin, params.bottomMargin);
                manager.setObstacle_location_y(i, -manager.get_obstacle_locationY(i));
                manager.setHasObstacle(i, false);
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
        oopsSound();
        manager.setBonusScore(-100);
        goneObstacle(i);
    }

    protected void oopsSound(Void... params) {
        MediaPlayer player = MediaPlayer.create(this.context, R.raw.oops);
        player.setLooping(false); // Set looping
        player.setVolume(1.0f, 1.0f);
        player.start();

    }

    ///If a UFO has reached the end of the lane or collided with the spaceship make it invisible.

    private void goneObstacle(int i){
        if(manager.getObstacleType(i) == manager.OBS_TYPE_UFO)
            game_IMG_UFO[i].setVisibility(View.GONE);
        else
            game_IMG_Burger[i].setVisibility(View.GONE);
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
                openGameOver();
            }

    }

    private void openGameOver() {
        Intent gameOverIntent = new Intent(this, Game_over.class);
        gameOverIntent.putExtra(Game_over.KEY_GAME_SCORE, manager.getScore());
        gameOverIntent.putExtra(Game_over.KEY_GAME_TYPE, this.gameType);
        timer.cancel();
        startActivity(gameOverIntent);
        finish();
    }

    ///When clicking fab buttons

    private void clicked(int dir) {
        if(dir>0)goRight();
        else goLeft();
    }

    ///If clicking right and the spaceship is not at the far end - go right

    private void invisible_buttons() {
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

    @Override
    protected void onResume() {
        super.onResume();
        if(gameType == Main_Menu.SNSR_MODE)
            tilt_detector.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gameType == Main_Menu.SNSR_MODE)
            tilt_detector.stop();
    }

    private void findViews(int gameType) {
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_FAB_left = findViewById(R.id.game_FAB_left);
        game_FAB_right = findViewById(R.id.game_FAB_right);
        game_IMG_spaceships = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_spaceship1),
                findViewById(R.id.game_IMG_spaceship2),
                findViewById(R.id.game_IMG_spaceship3),
                findViewById(R.id.game_IMG_spaceship4),
                findViewById(R.id.game_IMG_spaceship5)

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
                findViewById(R.id.game_IMG_ufo4),
                findViewById(R.id.game_IMG_ufo5)
        };
        game_IMG_Burger = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_burger1),
                findViewById(R.id.game_IMG_burger2),
                findViewById(R.id.game_IMG_burger3),
                findViewById(R.id.game_IMG_burger4),
                findViewById(R.id.game_IMG_burger5)
        };
        game_LANE_lane = new LinearLayout[]{
                findViewById(R.id.game_LIST_lane1),
                findViewById(R.id.game_LIST_lane2),
                findViewById(R.id.game_LIST_lane3),
                findViewById(R.id.game_LIST_lane4),
                findViewById(R.id.game_LIST_lane5)
        };
        game_ET_toast = findViewById(R.id.game_ET_toast);
    }



}