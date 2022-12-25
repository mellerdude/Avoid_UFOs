package com.example.game3lanes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.game3lanes.interfaces.LocationCallback;
import com.example.game3lanes.views.List_Fragment;
import com.example.game3lanes.views.Map_Fragment;

public class Score_Activity extends AppCompatActivity {
    private List_Fragment listFragment;
    private Map_Fragment mapFragment;


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void coordinates(String key) {
            showUserLocation(key);
        }
    };
    private void showUserLocation(String key) {
        double x = Double.parseDouble(Score_DB.getInstance().getScore(key).get(2));
        double y = Double.parseDouble(Score_DB.getInstance().getScore(key).get(3));
        mapFragment.zoom(x, y);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);

        listFragment = new List_Fragment();
        listFragment.setCallback(locationCallback);
        mapFragment = new Map_Fragment();

        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_map, mapFragment).commit();
    }
}
