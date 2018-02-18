package com.airatikuzzz.radio.stations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maira on 03.02.2018.
 */

public class Category {
    public String title;
    public List<RadioStation> stations = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RadioStation> getStations() {
        return stations;
    }

    public void setStations(List<RadioStation> stations) {
        this.stations = stations;
    }

    public Category(String title) {
            this.title = title;
    }
}
