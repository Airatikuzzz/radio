package com.airatikuzzz.radio.fragments;



import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airatikuzzz.radio.ItemMovedEvent;
import com.airatikuzzz.radio.R;
import com.airatikuzzz.radio.interfaces.OnItemClickCallback;
import com.airatikuzzz.radio.player.RadioManager;
import com.airatikuzzz.radio.player.RadioService;
import com.airatikuzzz.radio.stations.RadioStation;
import com.airatikuzzz.radio.stations.StationsLab;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {
    private FavVH mHolder;
    RecyclerView mRecyclerView;
    FavStationsAdapter mAdapter;
    List<RadioStation> mFavStations;
    FirebaseStorage storage = FirebaseStorage.getInstance();


    OnItemClickCallback mOnItemClickCallback;
    OnItemClickCallback mOnItemClickCallbackForService;


    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnItemClickCallback = (OnItemClickCallback) getActivity();
        mOnItemClickCallbackForService = RadioManager.getService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favourites, container, false);

        mRecyclerView = v.findViewById(R.id.recycler_fav);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    public void updateUI(){
        StationsLab stationsLab = StationsLab.get(getActivity());

        List<RadioStation> radioStations = stationsLab.getRadioStations();
        Log.d("kek1","lol loaded"+ radioStations.toString());
        if(radioStations.size() == 0){
            mRecyclerView.setAdapter(new EmptyAdapter());
            mAdapter = null;
        }
        else {
            if(mAdapter == null) {
                mAdapter = new FavStationsAdapter(radioStations);
                mRecyclerView.setAdapter(mAdapter);
            }
            else
            {
                mAdapter.setRadioStations(radioStations);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
    class FavStationsAdapter extends RecyclerView.Adapter<FavVH>{

        List<RadioStation> mStationList;

        public List<RadioStation> getStationList() {
            return mStationList;
        }

        public FavStationsAdapter(List<RadioStation> stationList) {
            mStationList = stationList;
        }

        @Override
        public FavVH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_station,parent,false);
            return new FavVH(view);
        }

        @Override
        public void onBindViewHolder(FavVH holder, int position) {
            RadioStation station = mStationList.get(position);
            holder.bindHolder(station);
        }

        @Override
        public int getItemCount() {
            return mStationList.size();
        }
        public void setRadioStations(List<RadioStation> stations){
            mStationList = stations;
        }
    }

    class FavVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView icon;
        private RadioStation mStation;

        public FavVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.station_title);
            icon = itemView.findViewById(R.id.img_view);
            itemView.setOnClickListener(this);
        }

        public void bindHolder(RadioStation station){
            mHolder = this;
            mStation = station;
            title.setText(station.getTitle());
            StorageReference storageRef = storage.getReference().child(station.getIconUrl());
            Glide.with(getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .error(R.drawable.button_beatbox_pressed)
                    .into(icon);
        }


        @Override
        public void onClick(View view) {
            mOnItemClickCallbackForService = RadioManager.getService();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child(mStation.getIconUrl());
            Glide.with(getContext())
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(52,52) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            mStation.setIcon(resource); // Possibly runOnUiThread()
                            mOnItemClickCallback.onClickItem(mStation,0);
                            mOnItemClickCallbackForService.onClickItem(mStation, 0);
                        }
                    });
            mOnItemClickCallback.onClickItem(mStation, 1);
            mOnItemClickCallbackForService.onClickItem(mStation, 0);
        }
    }

    @Subscribe
    public void onEvent(ItemMovedEvent event){
        updateUI();
    }



    private class EmptyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_empty, parent, false);
            return new RecyclerView.ViewHolder(view) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("kek1", "onstart");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("kek1", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("kek1", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("kek1", "onstop");
        EventBus.getDefault().unregister(this);
    }
}
