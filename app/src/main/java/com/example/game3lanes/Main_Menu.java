package com.example.game3lanes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Main_Menu extends AppCompatActivity {

    private MaterialButton main_BTN_ButtonGameFast;
    private MaterialButton main_BTN_ButtonGameSlow;
    private MaterialButton main_BTN_SensorGame;
    private MaterialButton main_BTN_Scores;

    private final String buttonGameFastText = "Button Lane Game\nFAST";
    private final String buttonGameTextSlow = "Button Lane Game\nSLOW";
    private final String sensorGameText = "Sensor Lane Game";
    private final String scoreText = "Hi - Scores";

    public final static int BTN_MODE_FAST = 0;
    public final static int BTN_MODE_SLOW = 1;
    public final static int SNSR_MODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        findViews();

        main_BTN_ButtonGameFast.setOnClickListener(view -> {
            clickedButtonFastGame();
        });
        main_BTN_ButtonGameSlow.setOnClickListener(view -> {
            clickedButtonSlowGame();
        });
        main_BTN_SensorGame.setOnClickListener(view -> {
            clickedSensorGame();
        });

        main_BTN_Scores.setOnClickListener(view -> {
            clickedScores();
        });


    }

    private void clickedButtonSlowGame() {
        openGame(BTN_MODE_SLOW);
    }

    private void clickedButtonFastGame() {
        openGame(BTN_MODE_FAST);
    }

    private void openGame(int gameType) {
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra(GameActivity.KEY_GAME_TYPE, gameType);
        startActivity(gameIntent);
        finish();
    }

    private void clickedSensorGame() {
        openGame(SNSR_MODE);

    }

    private void clickedScores() {
        openScoreScreen();

    }

    private void openScoreScreen() {
        Intent scoreIntent = new Intent(this, Score_Activity.class);
        startActivity(scoreIntent);
        finish();
    }


    private void findViews() {
        main_BTN_ButtonGameFast = findViewById(R.id.main_BTN_ButtonGameFast);
        main_BTN_ButtonGameSlow = findViewById(R.id.main_BTN_ButtonGameSlow);
        main_BTN_ButtonGameFast.setText(buttonGameFastText);
        main_BTN_ButtonGameSlow.setText(buttonGameTextSlow);
        main_BTN_SensorGame = findViewById(R.id.main_BTN_SensorGame);
        main_BTN_SensorGame.setText(sensorGameText);
        main_BTN_Scores = findViewById(R.id.main_BTN_Scores);
        main_BTN_Scores.setText(scoreText);


    }



}
