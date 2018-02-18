package com.airatikuzzz.radio.stations;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airatikuzzz.radio.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maira on 26.01.2018.
 */

public class StationsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> titlesOfStationsList;
    private HashMap<String, List<RadioStation>> stationsHashMap;

    private HashMap<String, List<RadioStation>> reservestationsHashMap;

    public StationsAdapter(Context context, List<String> titlesOfStationsList,
                           HashMap<String, List<RadioStation>> stationsHashMap) {
        this.context = context;
        this.titlesOfStationsList = titlesOfStationsList;
        this.stationsHashMap = stationsHashMap;
        this.reservestationsHashMap = new HashMap<>();
        this.reservestationsHashMap.putAll(this.stationsHashMap);

    }
    @Override
    public int getGroupCount() {
        return titlesOfStationsList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return stationsHashMap.get(
                titlesOfStationsList.get(i)
        ).size();
    }

    @Override
    public Object getGroup(int i) {
        return titlesOfStationsList.get(i);

    }

    @Override
    public Object getChild(int i, int i1) {
        return stationsHashMap.get(
                titlesOfStationsList.get(i)
        ).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String listTitle = (String) getGroup(i);
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.list_item_category, null);
        }
        TextView listTitleTextView = (TextView) view
                .findViewById(R.id.category_title);
        listTitleTextView.setAllCaps(true);
        //listTitleTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        listTitleTextView.setText(listTitle);
        return view;    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        RadioStation expListText = (RadioStation) getChild(i, i1);
        if(view==null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.list_item_station, null);
        }
        TextView  nameSt = view.findViewById(R.id.station_title);
        ImageView imageView = view.findViewById(R.id.img_view);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference().child(expListText.getIconUrl());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .error(R.drawable.button_beatbox_pressed)
                .into(imageView);

        nameSt.setText(expListText.getTitle());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void filterData(String query){
        HashMap<String, List<RadioStation>> newHashmap = new HashMap<>();
        List<RadioStation> queryStations = new ArrayList<>();
        query = query.toLowerCase();
        stationsHashMap.clear();
        titlesOfStationsList.clear();

        if(query.isEmpty()){
            stationsHashMap.putAll(reservestationsHashMap);
            titlesOfStationsList.addAll(reservestationsHashMap.keySet());
        }
        else {
            for(String category: reservestationsHashMap.keySet()){
                for(RadioStation r : reservestationsHashMap.get(category)){
                    if(r.getTitle().toLowerCase().contains(query)){
                        queryStations.add(r);
                    }
                }
                List<RadioStation> newList = new ArrayList<>();
                newList.addAll(queryStations);
                newHashmap.put(category, newList);
                queryStations.clear();
            }
            stationsHashMap.clear();
            stationsHashMap.putAll(newHashmap);
            titlesOfStationsList.clear();
            titlesOfStationsList.addAll(newHashmap.keySet());
        }
        notifyDataSetChanged();

    }
}