package com.airatikuzzz.radio;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by maira on 29.01.2018.
 */

public class StationPreferences {
    private static final String PREF_STATION_JSON= "basketdatajson";

    public static String getStoredQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_STATION_JSON, null);
    }

    public static void setStoredQuery(Context context, String data){
        // Log.d("kek2", data);
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_STATION_JSON, data)
                .apply();
    }
}
