package com.airatikuzzz.radio.stations;

import android.content.Context;

import com.airatikuzzz.radio.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maira on 25.01.2018.
 */

public class StationsLab {
    public HashMap<String, List<RadioStation>> getExpDetails() {
        return expDetails;
    }

    HashMap<String, List<RadioStation>> expDetails = new HashMap<>();

    public List<RadioStation> getStationsList() {
        return mRadioStations;
    }

    List<RadioStation> mRadioStations;

    public StationsLab(Context context){
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<RadioStation>>(){}.getType();
        Reader reader = new InputStreamReader(context.getResources().openRawResource(R.raw.shoutcasts));
        mRadioStations = gson.fromJson(reader, type);
        expDetails.put("Станции", mRadioStations);
    }
}
