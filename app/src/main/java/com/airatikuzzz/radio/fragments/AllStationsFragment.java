package com.airatikuzzz.radio.fragments;



import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.airatikuzzz.radio.R;
import com.airatikuzzz.radio.player.RadioManager;
import com.airatikuzzz.radio.stations.RadioStation;
import com.airatikuzzz.radio.stations.StationsAdapter;
import com.airatikuzzz.radio.stations.StationsLab;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AllStationsFragment extends Fragment {
    ListView mListView;
    IonClick mIonClick;
    IonClick mIonClickForService;

    ExpandableListView expListView;
    ExpandableListAdapter expListAdapter;


    List<String> expListTitle;
    HashMap<String, List<RadioStation>> expListDetail;

    public AllStationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIonClick = (IonClick) getActivity();
//        Log.d("kek", mIonClickForService.toString());
    }

    public interface IonClick{
        void onClickItem(RadioStation station);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_stations, container, false);

        expListView = (ExpandableListView) v.findViewById(R.id.expListView);
        expListDetail = new StationsLab(getContext()).getExpDetails();

        expListTitle = new ArrayList<>(expListDetail.keySet());
        expListAdapter = new StationsAdapter(getContext(), expListTitle, expListDetail);

        expListView.setAdapter(expListAdapter);
        /*expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getContext(),
                        expListTitle.get(groupPosition) + " Список раскрыт.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getContext(),
                        expListTitle.get(groupPosition) + " Список скрыт.",
                        Toast.LENGTH_SHORT).show();

            }
        });*/

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                mIonClickForService = RadioManager.getService();
                RadioStation r = expListDetail.get(expListTitle.get(groupPosition))
                        .get(childPosition);
                loadBitmap("https://image.ibb.co/butrtb/104_2.png");
                r.setIcon(icon);
                mIonClick.onClickItem(r);
                mIonClickForService.onClickItem(r);
                Log.d("kek", "clicked on allsttions" + r.getTitle());
                return false;
            }
        });

        return v;
    }

    private Target loadtarget;
    private Bitmap icon;

    public void loadBitmap(String url) {

        if (loadtarget == null) loadtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // do something with the Bitmap
                handleLoadedBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

        };

        Picasso.with(getContext()).load(url).into(loadtarget);
    }

    public void handleLoadedBitmap(Bitmap b) {
        // do something here
        icon = b;
    }




}
