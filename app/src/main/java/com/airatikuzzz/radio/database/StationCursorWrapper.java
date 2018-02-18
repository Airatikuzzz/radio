package com.airatikuzzz.radio.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.airatikuzzz.radio.database.StationDbSchema.StationTable;
import com.airatikuzzz.radio.stations.RadioStation;

import java.util.Date;
import java.util.UUID;


/**
 * Created by maira on 04.07.2017.
 */

public class StationCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public StationCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public RadioStation getRadioStation(){
        String title = getString(getColumnIndex(StationTable.Cols.TITLE));
        String url = getString(getColumnIndex(StationTable.Cols.URL));
        String iconUrl = getString(getColumnIndex(StationTable.Cols.ICONURL));
        String info = getString(getColumnIndex(StationTable.Cols.INFO));

        RadioStation station = new RadioStation();
        station.setTitle(title);
        station.setUrl(url);
        station.setIconUrl(iconUrl);
        station.setInfo(info);
        return station;
    }
}
