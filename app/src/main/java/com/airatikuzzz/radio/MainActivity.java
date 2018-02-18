package com.airatikuzzz.radio;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airatikuzzz.radio.fragments.AllStationsFragment;
import com.airatikuzzz.radio.fragments.FavouritesFragment;
import com.airatikuzzz.radio.interfaces.OnItemClickCallback;
import com.airatikuzzz.radio.player.PlaybackStatus;
import com.airatikuzzz.radio.player.RadioManager;
import com.airatikuzzz.radio.player.RadioService;
import com.airatikuzzz.radio.stations.RadioStation;
import com.airatikuzzz.radio.stations.StationsLab;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnItemClickCallback{

    RadioStation mCurrentStation;
    boolean isSelectedFirstTab;

    @BindView(R.id.iv_solo)
    ImageView mImageView;


    Toolbar toolbar;
    SearchView searchView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    BottomSheetBehavior bottomSheetBehavior;
    StationsLab stationsLab;

    @BindView(R.id.playTrigger)
    ImageButton trigger;

    @BindView(R.id.name)
    TextView textView;

    @BindView(R.id.playTrigger1)
    ImageButton trigger1;

    @BindView(R.id.name1)
    TextView textView1;

    @BindView(R.id.sub_player)
    View subPlayer1;

    @BindView(R.id.btn_into_fav)
            Button btnIntoFav;
    @BindView(R.id.btn_into_site)
            Button btnIntoSite;

    @BindView(R.id.image_view_sub)
    ImageView mImageViewSub;

    @BindView(R.id.seek_bar)
    SeekBar mSeekBarVolume;

   // @BindView(R.id.bottom_sheet)
   // LinearLayout mLayout;

    LinearLayout mSubForFirst;

    AudioManager mAudioManager;
    RadioManager radioManager;

    int state = -99;
    private String KEY_STATION = "aira";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d("kek2", "now inlab"+System.currentTimeMillis());
        radioManager = RadioManager.with(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(1).select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    isSelectedFirstTab = true;
                }
                else { isSelectedFirstTab = false;}
                invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ButterKnife.bind(this);

        RadioService service = RadioManager.getService();
        if(service!=null ){
            if(service.getStatus() == PlaybackStatus.PLAYING || service.getStatus()==PlaybackStatus.PAUSED ){
                mCurrentStation = RadioManager.getService().getCurrentStation();
                Log.d("kek1", mCurrentStation.toString());
                updateUI();
            }
        }
        stationsLab = StationsLab.get(this);
        LinearLayout linearLayout = findViewById(R.id.bottom_sheet_first_screen);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //subPlayer1.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                subPlayer1.animate().alpha(1 - slideOffset*2).setDuration(0).start();
            }
        });

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSeekBarVolume.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        mSeekBarVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        mSeekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onStart() {

        super.onStart();

        if(state != -99){
            bottomSheetBehavior.setState(state);
        }

        EventBus.getDefault().register(this);

        radioManager.bind();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        state = bottomSheetBehavior.getState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        radioManager.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else if(!searchView.isIconified()) {
            searchView.setIconified(true);
        }
        else super.onBackPressed();
    }

    @Subscribe
    public void onEvent(String status){

        switch (status){

            case PlaybackStatus.LOADING:

                // loading

                break;

            case PlaybackStatus.ERROR:

                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();
                break;

        }

        trigger.setImageResource(status.equals(PlaybackStatus.PLAYING)
                ? R.drawable.ic_pause_black
                : R.drawable.ic_play_arrow_black);
        trigger1.setImageResource(status.equals(PlaybackStatus.PLAYING)
                ? R.drawable.ic_pause_black_big
                : R.drawable.ic_play_arrow_black_big);

    }

    @OnClick({R.id.playTrigger,R.id.playTrigger1})
    public void onClicked(){
        if(mCurrentStation==null) return;
        radioManager.playOrPause(mCurrentStation.getUrl());
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllStationsFragment(), "Категории");
        adapter.addFragment(new FavouritesFragment(), "Избранное");
        adapter.addFragment(new BlankFragment(), "Настройки");
        viewPager.setAdapter(adapter);
    }

    public void updateSub(){
        textView.setText(mCurrentStation.getTitle());
        mImageViewSub.setImageBitmap(mCurrentStation.getIcon());
        trigger.setImageResource(radioManager.isPlaying()
                ? R.drawable.ic_pause_black
                : R.drawable.ic_play_arrow_black);
    }

    public void updateUI(){
        textView1.setText(mCurrentStation.getTitle());
        //mImageViewSub1.setImageBitmap(mCurrentStation.getIcon());
        trigger1.setImageResource(radioManager.isPlaying()
                ? R.drawable.ic_pause_black_big
                : R.drawable.ic_play_arrow_black_big);
        updateSub();
        setButtonFavourite();
        if(mCurrentStation.getIcon()!=null){
            mImageView.setImageBitmap(mCurrentStation.getIcon());
        }
    }

    @Override
    public void onClickItem(final RadioStation station, int code) {

        if(code == 0){
            mCurrentStation = station;
            updateUI();
            mImageViewSub.setImageBitmap(mCurrentStation.getIcon());
        }
        else {
            mCurrentStation = station;
            updateUI();
            radioManager.playOrPause(mCurrentStation.getUrl());
        }

    }

    public void setButtonFavourite(){
        if(isFavourite()){
            btnIntoFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_black_24dp, 0,0,0);
            btnIntoFav.setText("Добавлено");
        }
        else{
            btnIntoFav.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_border_black_24dp, 0,0,0);
            btnIntoFav.setText(R.string.into_fav);
        }
    }
    public boolean isFavourite(){
        //Log.d("kek1", "in isfav"+ mCurrentStation.getUrl() +stationsLab.getRadioStation(mCurrentStation.getUrl()).toString());
        stationsLab = StationsLab.get(this);
        for(RadioStation r : stationsLab.getRadioStations() ){
            if(r.getUrl().equals(mCurrentStation.getUrl())){
                return true;
            }
        }
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        MenuItem item = menu.findItem(R.id.action_search);
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if(isSelectedFirstTab) {
            item.setVisible(true);
        }
        else item.setVisible(false);
        searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                EventBus.getDefault().post(new Stroka(s));
                Log.d("kek2", "onQueryTExtSubmit " + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                EventBus.getDefault().post(new Stroka(s));
                Log.d("kek2", "onQueryTExtChange " + s);

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                EventBus.getDefault().post(new Stroka(""));
                Log.d("kek2", "onclose");

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.clear_fav){
            List<RadioStation> radioStations = stationsLab.getRadioStations();
            for(RadioStation station : radioStations){
                stationsLab.deleteRadioStation(station);
            }
            EventBus.getDefault().post(new ItemMovedEvent(mCurrentStation, "prek"));
            setButtonFavourite();
        }
        else if(item.getItemId() == R.id.action_settings){
        }

        return true;
    }

    @OnClick({R.id.btn_into_fav, R.id.btn_into_site})
    public void onClickButtons(View view){
        String action;
        switch (view.getId()){
            case R.id.btn_into_fav:
                if(!isFavourite()) {
                    Log.d("kek1", "in !isfav");
                    stationsLab.addRadioStation(mCurrentStation);
                    action = ItemMovedEvent.ACTION_ADDED;
                }
                else {
                    Log.d("kek1", "in isfav");
                    stationsLab.deleteRadioStation(mCurrentStation);
                    action = ItemMovedEvent.ACTION_REMOVED;
                }
                EventBus.getDefault().post(new ItemMovedEvent(mCurrentStation, action));
                setButtonFavourite();
                break;
            case R.id.btn_into_site:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrentStation.getInfo()));
                startActivity(i)    ;
                break;
        }
    }

    @OnClick(R.id.sub_player)
    public void onClickSub(View v){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

}
