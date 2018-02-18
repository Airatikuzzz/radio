package com.airatikuzzz.radio.stations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.airatikuzzz.radio.R;
import com.airatikuzzz.radio.database.StationBaseHelper;
import com.airatikuzzz.radio.database.StationCursorWrapper;
import com.airatikuzzz.radio.database.StationDbSchema;
import com.airatikuzzz.radio.database.StationDbSchema.StationTable;
import com.airatikuzzz.radio.player.RadioService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maira on 25.01.2018.
 */

public class StationsLab {

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static StationsLab sStationsLab;

    HashMap<String, List<RadioStation>> expDetails = new LinkedHashMap<>();


    public List<RadioStation> getStationsList() {
        return mRadioStations;
    }

    List<RadioStation> mRadioStations = new ArrayList<>();

    public HashMap<String, List<RadioStation>> getExpDetails() {
        return expDetails;
    }


    public static StationsLab get(Context context) {
        if (sStationsLab == null) {
            sStationsLab = new StationsLab(context);
        }
        return sStationsLab;
    }

    public StationsLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new StationBaseHelper(mContext).getWritableDatabase();


        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<RadioStation>>(){}.getType();
        DatabaseReference mDatabase;
        List<String> categories = Arrays.asList("Лучшее", "Поп", "Татарские");

        for(final String category : categories) {
            Log.d("kek3", category);
            mDatabase = FirebaseDatabase.getInstance().getReference(category);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<RadioStation> radios = new ArrayList<>();
                    String s = category;
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        RadioStation r = d.getValue(RadioStation.class);
                        radios.add(r);
                    }
                    expDetails.put(s, radios);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        /*Reader reader = new InputStreamReader(context.getResources().openRawResource(R.raw.best));
        mRadioStations = gson.fromJson(reader, type);
        expDetails.put("Лучшее", mRadioStations);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Лучшее");

        for(RadioStation r : mRadioStations){
            mDatabase.push().setValue(r);
        }
        
        reader = new InputStreamReader(context.getResources().openRawResource(R.raw.pop));
        mRadioStations = gson.fromJson(reader, type);
        expDetails.put("Поп", mRadioStations);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Поп");

        for(RadioStation r : mRadioStations){
            mDatabase.push().setValue(r);
        }
        List<RadioStation> radios = new ArrayList<>();


        reader = new InputStreamReader(context.getResources().openRawResource(R.raw.tatar));
        radios = gson.fromJson(reader, type);
        Log.d("kek2", radios.toString());
        expDetails.put("Татарские", radios);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Татарские");

        for(RadioStation r : radios){
            mDatabase.push().setValue(r);
        }

        //radios.clear();

        /*List<Category> categories = new ArrayList<>();
        Collections.addAll(categories,
                new Category("Лучшее"),
                new Category("Поп"),
                new Category("Татарские"));
        for(Category category : categories){
            radios = new ArrayList<>();
            radios = fromFireStore(category.getTitle());
            expDetails.put(category.getTitle(), radios);
            radios.clear();
        }
        /*Category categoryBest = new Category("Лучшее");
        mRadioStations = fromFireStore(categoryBest.getTitle());
        categoryBest.setStations(mRadioStations);
        expDetails.put(categoryBest.getTitle(), categoryBest.getStations());
        mRadioStations.clear();
        Category categoryPop = new Category("Поп");
        mRadioStations = fromFireStore(categoryPop.getTitle());
        categoryPop.setStations(mRadioStations);
        expDetails.put(categoryPop.getTitle(), categoryPop.getStations());
        //toFireStore("Поп",mRadioStations);*/

    }


    /*public List<RadioStation> fromFireStore(String collection) {
        db = FirebaseFirestore.getInstance();
        final List<RadioStation> stations = new ArrayList<>();
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                RadioStation r = document.toObject(RadioStation.class);
                                stations.add(r);
                            }
                        } else {
                            Log.d("kek", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return stations;
    }

    public void toFireStore(String category, List<RadioStation> radioStations) {
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        CollectionReference stations = db1.collection(category);

        for (RadioStation station : radioStations) {
            stations.add(station);
        }
    }*/

    public void addRadioStation(RadioStation c) {
        ContentValues values = getContentValues(c);
        Log.d("kek1", values.toString());

        mDatabase.insert(StationTable.NAME, null, values);
    }

    public List<RadioStation> getRadioStations() {
        List<RadioStation> radioStations = new ArrayList<>();

        StationCursorWrapper cursor = queryRadioStations(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                radioStations.add(cursor.getRadioStation());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return radioStations;
    }

    public RadioStation getRadioStation(String url) {

        StationCursorWrapper cursor = queryRadioStations(StationTable.Cols.URL + " = ?", new String[]{url});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getRadioStation();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(RadioStation RadioStation) {
        ContentValues values = new ContentValues();
        values.put(StationTable.Cols.TITLE, RadioStation.getTitle());
        values.put(StationTable.Cols.URL, RadioStation.getUrl());
        values.put(StationTable.Cols.ICONURL, RadioStation.getIconUrl());
        values.put(StationTable.Cols.INFO, RadioStation.getInfo());
        return values;
    }

    private StationCursorWrapper queryRadioStations(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(StationTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new StationCursorWrapper(cursor);
    }

    public void deleteRadioStation(RadioStation radioStation) {
        mDatabase.delete(StationTable.NAME, StationTable.Cols.URL + " = ?", new String[]{radioStation.getUrl()});
    }
}
