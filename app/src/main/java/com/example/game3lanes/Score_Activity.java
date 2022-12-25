package com.example.game3lanes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;


public class Score_Activity extends AppCompatActivity {

    private static final String KEY_GAME_SCORE = "GAME_SCORE";
    private static final String KEY_GAME_NAME = "GAME_NAME";
    private MaterialButton[] score_BTN_scores;
    private MaterialButton score_BTN_menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hi_score);
        Score_DB.init(this);
        findViews();
        getScoreValues();
        score_BTN_menu.setOnClickListener(view -> {
            clickedMenu();
        });

    }

    private void clickedMenu() {
        Intent menuIntent = new Intent(this, Main_Menu.class);
        startActivity(menuIntent);
        finish();
    }

    private void getScoreValues() {
        int j = Score_DB.NUM_SCORE;
        for (int i =0; i < Score_DB.NUM_SCORE ; i++) {
            String name = Score_DB.getInstance().getScore("" + (i+1)).get(0);
            String score = Score_DB.getInstance().getScore("" + (i+1)).get(1);
            String full = "" + j + ". " + name + ": " + score;
            score_BTN_scores[i].setText(full);
            j--;
        }
    }

    private void findViews() {
        score_BTN_menu = findViewById(R.id.score_BTN_menu);
        score_BTN_scores = new MaterialButton[]{
                findViewById(R.id.score_BTN_score10),
                findViewById(R.id.score_BTN_score9),
                findViewById(R.id.score_BTN_score8),
                findViewById(R.id.score_BTN_score7),
                findViewById(R.id.score_BTN_score6),
                findViewById(R.id.score_BTN_score5),
                findViewById(R.id.score_BTN_score4),
                findViewById(R.id.score_BTN_score3),
                findViewById(R.id.score_BTN_score2),
                findViewById(R.id.score_BTN_score1)
        };
    }

}
