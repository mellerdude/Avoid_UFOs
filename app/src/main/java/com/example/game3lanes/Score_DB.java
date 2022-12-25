package com.example.game3lanes;

import static android.content.Context.LOCATION_SERVICE;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score_DB {
    private static final String DB_FILE = "DB_FILE";
    public static final int NUM_SCORE = 10;
    private static Score_DB instance = null;
    private SharedPreferences preferences;
    private Context context;

    private Score_DB(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(DB_FILE,Context.MODE_PRIVATE);
        if(preferences.getString("" + 1, null) == null)
            enterDefaultScores(preferences);
    }

    public static void init(Context context){
        if (instance == null)
            instance = new Score_DB(context);
    }

    public static Score_DB getInstance() {
        return instance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public List<String> getScore(String key){
        String json = getString(key, "1");
        List<String> values = new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
        return values;
    }

    public void addScore(int place, long score, String name){
        Map<String, List<String>> values = new HashMap<>();
        Location location = new Location(context);
        double[] locations = location.getLocation();
        values.put("" + place, Arrays.asList(name, String.valueOf(score), String.valueOf(locations[0]), String.valueOf(locations[1])));
//        for (int i=0;i<10;i++)
//            values.put("" + i, getScore("" + i));
//        Collections.sort(values, new Comparator<Map.Entry<String, List<String>>>() {

        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            String json = new Gson().toJson(entry.getValue());
            // Save the JSON string in the shared preferences
            putString(entry.getKey(), json);
        }
    }

    public int isGoodScore(long score){
        int toReplace = 0;
        if(score != 0) {
            for (int i = Score_DB.NUM_SCORE; i > 0; i--) {
                String currScore = Score_DB.getInstance().getScore("" + (i)).get(1);
                if (score > Integer.parseInt(currScore)) {
                    toReplace = i;
                    break;
                }
            }
            return toReplace;
        }
        return toReplace;

    }


    public void enterDefaultScores(SharedPreferences preferences){
        Map<String, List<String>> values = new HashMap<>();

        for (int i=0; i<NUM_SCORE; i++){
            values.put("" + (i + 1), Arrays.asList("Bereshit", String.valueOf((i+1) * 100), String.valueOf(0.0), String.valueOf(0.0)));
        }
        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            String json = new Gson().toJson(entry.getValue());
            // Save the JSON string in the shared preferences
            putString(entry.getKey(), json);
        }
    }


}
