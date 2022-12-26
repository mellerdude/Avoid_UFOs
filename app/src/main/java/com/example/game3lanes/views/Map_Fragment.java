package com.example.game3lanes.views;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.game3lanes.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textview.MaterialTextView;


public class Map_Fragment extends Fragment {

    private LinearLayout map_View_holder;
    SupportMapFragment mapFragment;
    int zoom = 0;
    private final int ZOOM_BY = 5;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_MAP_show);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        map_View_holder = view.findViewById(R.id.map_View_holder);
    }

    public void zoom(double latitude, double longitude) {
        if(mapFragment!=null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    // Your code here

                    LatLng location = new LatLng(latitude, longitude);
                    googleMap.addMarker(new MarkerOptions().position(location).title("Marker Title"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    googleMap.stopAnimation();
                    if(zoom == 0) {
                        zoom = ZOOM_BY;
                        googleMap.moveCamera(CameraUpdateFactory.zoomBy(10));
                    }

                }
            });
        }
    }
}
