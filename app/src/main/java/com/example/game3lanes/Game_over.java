package com.example.game3lanes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.game3lanes.views.List_Fragment;
import com.google.android.material.button.MaterialButton;


    public class Game_over extends AppCompatActivity implements LocationListener {

        public static final String KEY_GAME_SCORE = "GAME_SCORE";
        public static final String KEY_GAME_TYPE = "GAME_TYPE";
        private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
        public final String WIN = "You Win! Top ten skills!";
        public final String LOSE = "You Lost! Try again!";
        private MaterialButton gameover_BTN_gameOver;
        private MaterialButton gameover_BTN_FinalScore;
        private MaterialButton gameover_BTN_menu;
        private EditText gameover_INPUTTEXT_name;
        private TextView gameover_TXT_gamescore;
        private MaterialButton gameover_BTN_enter;
        private MaterialButton gameover_BTN_play;
        private long score = 0;
        private int place = 0;
        private int gameType = 0;
        private double lati = 0;
        private double longi = 0;
        LocationManager locationManager;
        int locationChanged = 0;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.game_over);
            Intent previousIntent = getIntent();
            Score_DB.init(this);
            score = previousIntent.getLongExtra(KEY_GAME_SCORE, 0);
            place = Score_DB.getInstance().isGoodScore(score);
            this.gameType = previousIntent.getIntExtra(KEY_GAME_TYPE, 0);
            findViews();
            setViews(place);
        }

        private void setViews(int place) {
            if (place > 0) {
                gameover_BTN_gameOver.setText(WIN);
                gameover_TXT_gamescore.setText("" + score);
                gameover_BTN_enter.setOnClickListener(view -> {
                    clickedEnter();
                });

            } else {
                gameover_BTN_gameOver.setText(LOSE);
                gameover_BTN_enter.setVisibility(View.INVISIBLE);
                gameover_INPUTTEXT_name.setVisibility(View.INVISIBLE);
            }
            gameover_BTN_play.setOnClickListener(view -> {
                clickedPlay();
            });

            gameover_BTN_menu.setOnClickListener(view -> {
                clickedMenu();
            });
        }

        private void clickedMenu() {
            Intent menuIntent = new Intent(this, Main_Menu.class);
            startActivity(menuIntent);
            finish();
        }

        private void clickedPlay() {
            Intent gameIntent = new Intent(this, GameActivity.class);
            gameIntent.putExtra(GameActivity.KEY_GAME_TYPE, this.gameType);
            startActivity(gameIntent);
            finish();

        }

        private void clickedEnter() {
            getLocation();

        }

        private void findViews() {
            gameover_BTN_gameOver = findViewById(R.id.gameover_BTN_gameOver);
            gameover_BTN_FinalScore = findViewById(R.id.gameover_BTN_FinalScore);
            gameover_INPUTTEXT_name = findViewById(R.id.gameover_INPUTTEXT_name);
            gameover_BTN_enter = findViewById(R.id.gameover_BTN_enter);
            gameover_BTN_play = findViewById(R.id.gameover_BTN_play);
            gameover_TXT_gamescore = findViewById(R.id.gameover_TXT_gamescore);
            gameover_BTN_menu = findViewById(R.id.gameover_BTN_menu);

        }


        void getLocation() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                try {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            longi = location.getLongitude();
            lati = location.getLatitude();
            Intent scoreIntent = new Intent(this, Score_Activity.class);
            Score_DB.getInstance().addScore(place, score, String.valueOf(gameover_INPUTTEXT_name.getText()), lati, longi);
            startActivity(scoreIntent);
            finish();
        }
    }
