package com.example.game3lanes.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.game3lanes.Main_Menu;
import com.example.game3lanes.R;
import com.example.game3lanes.Score_DB;
import com.example.game3lanes.interfaces.LocationCallback;
import com.google.android.material.button.MaterialButton;


public class List_Fragment extends Fragment {

    private static final String KEY_GAME_SCORE = "GAME_SCORE";
    private static final String KEY_GAME_NAME = "GAME_NAME";
    private MaterialButton[] score_BTN_scores;
    private MaterialButton score_BTN_menu;
    private Context context;
    private LocationCallback callback;
    private int currently;

    public void setCallback(LocationCallback callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hi_score, container, false);
        this.context = getActivity();
        super.onCreate(savedInstanceState);
        Score_DB.init(this.context);
        findViews(view);
        getScoreValues();

        for(int i=0; i<10;i++){
            currently = i;
            score_BTN_scores[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    clickedScore(currently);
                }
            });
        }
        score_BTN_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedMenu();
            }
        });
        return view;
    }

    private void clickedScore(int i) {
        callback.coordinates(String.valueOf(i));
    }

    private void clickedMenu() {
        Intent menuIntent = new Intent(this.context, Main_Menu.class);
        startActivity(menuIntent);
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

    private void findViews(View view) {
        score_BTN_menu = view.findViewById(R.id.score_BTN_menu);
        score_BTN_scores = new MaterialButton[]{
                view.findViewById(R.id.score_BTN_score10),
                view.findViewById(R.id.score_BTN_score9),
                view.findViewById(R.id.score_BTN_score8),
                view.findViewById(R.id.score_BTN_score7),
                view.findViewById(R.id.score_BTN_score6),
                view.findViewById(R.id.score_BTN_score5),
                view.findViewById(R.id.score_BTN_score4),
                view.findViewById(R.id.score_BTN_score3),
                view.findViewById(R.id.score_BTN_score2),
                view.findViewById(R.id.score_BTN_score1)
        };
    }

}
