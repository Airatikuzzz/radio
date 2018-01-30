package com.airatikuzzz.radio;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.airatikuzzz.radio.fragments.AllStationsFragment;
import com.airatikuzzz.radio.fragments.FavouritesFragment;
import com.airatikuzzz.radio.player.PlaybackStatus;
import com.airatikuzzz.radio.player.RadioManager;
import com.airatikuzzz.radio.stations.RadioStation;
import com.google.gson.Gson;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AllStationsFragment.IonClick{

    RadioStation mCurrentStation;

    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    MaterialSearchView searchView;

    @BindView(R.id.playTrigger)
    ImageButton trigger;

    @BindView(R.id.name)
    TextView textView;

    @BindView(R.id.sub_player)
    View subPlayer;

    ImageView mImageViewSub;

    RadioManager radioManager;

    String streamURL;
    private String KEY_STATION = "aira";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        radioManager = RadioManager.with(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ButterKnife.bind(this);

        mImageViewSub = findViewById(R.id.image_view_sub);

        if(RadioManager.getService()!=null){
            mCurrentStation = RadioManager.getService().getCurrentStation();
            updateUI();
        }
    }

    @Override
    public void onStart() {

        super.onStart();

        EventBus.getDefault().register(this);
        radioManager.bind();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
        super.onBackPressed();
       // finish();
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

    }

    @OnClick(R.id.playTrigger)
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
    public void updateUI(){
        subPlayer.setVisibility(View.VISIBLE);
        textView.setText(mCurrentStation.getTitle().toString());
        mImageViewSub.setImageBitmap(mCurrentStation.getIcon());
        trigger.setImageResource(radioManager.isPlaying()
                ? R.drawable.ic_pause_black
                : R.drawable.ic_play_arrow_black);

    }

    @Override
    public void onClickItem(RadioStation station) {
        mCurrentStation = station;
        Log.d("kek", "clicked on main");
        textView.setText(station.getTitle());

        subPlayer.setVisibility(View.VISIBLE);

        mImageViewSub.setImageBitmap(mCurrentStation.getIcon());
        streamURL = station.getUrl();

        radioManager.playOrPause(streamURL);
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
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
