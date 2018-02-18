package com.airatikuzzz.radio.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.airatikuzzz.radio.database.StationDbSchema.StationTable;


/**
 * Created by maira on 02.07.2017.
 */

public class StationBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "stationbase.db";

    public StationBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + StationTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                StationTable.Cols.TITLE + ", " +
                StationTable.Cols.URL + ", " +
                StationTable.Cols.ICONURL +", " +
                StationTable.Cols.INFO +
                ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
