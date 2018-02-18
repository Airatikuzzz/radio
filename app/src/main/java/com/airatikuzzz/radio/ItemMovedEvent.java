package com.airatikuzzz.radio;

import com.airatikuzzz.radio.stations.RadioStation;

/**
 * Created by maira on 02.02.2018.
 */

public class ItemMovedEvent {
    private RadioStation station;
    private String action;
    public static final String ACTION_ADDED = "AIRATIKUZZZ.ADDED";
    public static final String ACTION_REMOVED = "AIRATIKUZZZ.REMOVED";

    public ItemMovedEvent(RadioStation station, String action) {
        this.station = station;
        this.action = action;
    }

    public RadioStation getStation() {
        return station;
    }

    public void setStation(RadioStation station) {
        this.station = station;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
