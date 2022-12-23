package com.example.game3lanes;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Main_Menu extends AppCompatActivity {

    private MaterialButton main_BTN_ButtonGame;
    private MaterialButton main_BTN_SensorGame;
    private MaterialButton main_BTN_Scores;

    private final String buttonGameText = "Button Lane Game";
    private final String sensorGameText = "Sensor Lane Game";
    private final String scoreText = "Hi - Scores";

    public final static int BTN_MODE = 0;
    public final static int SNSR_MODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        findViews();

        main_BTN_ButtonGame.setOnClickListener(view -> {
            clickedButtonGame();
        });
        main_BTN_SensorGame.setOnClickListener(view -> {
            clickedSensorGame();
        });

        main_BTN_Scores.setOnClickListener(view -> {
            clickedScores();
        });


    }



    private void clickedButtonGame() {
        openGame(BTN_MODE);
    }

    private void openGame(int gameType) {
        Intent buttonGameIntent = new Intent(this, GameActivity.class);
        buttonGameIntent.putExtra(GameActivity.KEY_GAME_TYPE, gameType);
        startActivity(buttonGameIntent);
        finish();
    }

    private void clickedSensorGame() {
        openGame(SNSR_MODE);

    }

    private void clickedScores() {
        openScoreScreen();

    }

    private void openScoreScreen() {
    }


    private void findViews() {
        main_BTN_ButtonGame = findViewById(R.id.main_BTN_ButtonGame);
        main_BTN_ButtonGame.setText(buttonGameText);
        main_BTN_SensorGame = findViewById(R.id.main_BTN_SensorGame);
        main_BTN_SensorGame.setText(sensorGameText);
        main_BTN_Scores = findViewById(R.id.main_BTN_Scores);
        main_BTN_Scores.setText(scoreText);


    }



}
