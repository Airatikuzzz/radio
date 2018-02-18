package com.airatikuzzz.radio.fragments;



import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;

import com.airatikuzzz.radio.ItemMovedEvent;
import com.airatikuzzz.radio.Stroka;
import com.airatikuzzz.radio.interfaces.OnItemClickCallback;
import com.airatikuzzz.radio.R;
import com.airatikuzzz.radio.player.RadioManager;
import com.airatikuzzz.radio.stations.RadioStation;
import com.airatikuzzz.radio.stations.StationsAdapter;
import com.airatikuzzz.radio.stations.StationsLab;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AllStationsFragment extends Fragment {
    ListView mListView;
    OnItemClickCallback mOnItemClickCallback;
    OnItemClickCallback mOnItemClickCallbackForService;

    ExpandableListView expListView;
    StationsAdapter expListAdapter;


    List<String> expListTitle;
    HashMap<String, List<RadioStation>> expListDetail;

    public AllStationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnItemClickCallback = (OnItemClickCallback) getActivity();
        Log.d("kek", "now");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_stations, container, false);

        expListView = (ExpandableListView) v.findViewById(R.id.expListView);
        expListDetail = StationsLab.get(getContext()).getExpDetails();


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
                mOnItemClickCallbackForService = RadioManager.getService();
                final RadioStation r = expListDetail.get(expListTitle.get(groupPosition))
                        .get(childPosition);
                final Bitmap icon;

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child(r.getIconUrl());
                Glide
                        .with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(52,52) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                r.setIcon(resource); // Possibly runOnUiThread()
                                mOnItemClickCallback.onClickItem(r,0);
                                mOnItemClickCallbackForService.onClickItem(r, 0);
                            }
                        });

                mOnItemClickCallback.onClickItem(r, 1);
                mOnItemClickCallbackForService.onClickItem(r, 0);
                return false;
            }
        });

        return v;
    }

    @Subscribe
    public void onEvent(Stroka query){
        expListAdapter.filterData(query.getQuery());
        expandAll();
    }

    private void expandAll() {
        int count = expListAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            expListView.expandGroup(i);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
}
