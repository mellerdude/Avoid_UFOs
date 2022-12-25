package com.example.game3lanes;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;

import androidx.annotation.NonNull;

public class Location {

    private LocationManager loc;
    private String ProviderName;
    private boolean isEnabled;
    private LocationProvider provider;
    public Location(Context context) {
        this.loc = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);

        this.ProviderName = loc.getBestProvider(criteria, true);


        this.provider = loc.getProvider(LocationManager.GPS_PROVIDER);

        isEnabled = loc.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!isEnabled)
            enableLocationSettings(context);


    }

    public double[] getLocation(){
        double[] locations = new double[2];
        final LocationListener listener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull android.location.Location location) {
                locations[0] = location.getLatitude();
                locations[1] = location.getLongitude();
            }
        };
        return locations;
    }

    private void enableLocationSettings(Context context) {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(settingsIntent);
    }
}
