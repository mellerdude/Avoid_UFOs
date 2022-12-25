package com.example.game3lanes.views;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.game3lanes.R;
import com.google.android.material.textview.MaterialTextView;


public class Map_Fragment extends Fragment {

    private MaterialTextView map_LBL_title;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        map_LBL_title = view.findViewById(R.id.map_LBL_title);
    }

    public void zoom(double latitude, double longitude) {
        map_LBL_title.setText(latitude  + "\n" + longitude);
    }
}
