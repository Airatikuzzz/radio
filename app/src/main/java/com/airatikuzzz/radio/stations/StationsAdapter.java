package com.airatikuzzz.radio.stations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.airatikuzzz.radio.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by maira on 26.01.2018.
 */

public class StationsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expListTitle;
    private HashMap<String, List<RadioStation>> expListDetail;

    public StationsAdapter(Context context, List<String> expListTitle,
                           HashMap<String, List<RadioStation>> expListDetail) {
        this.context = context;
        this.expListTitle = expListTitle;
        this.expListDetail = expListDetail;
    }
    @Override
    public int getGroupCount() {
        return expListTitle.size();

    }

    @Override
    public int getChildrenCount(int i) {
        return expListDetail.get(
                expListTitle.get(i)
        ).size();
    }

    @Override
    public Object getGroup(int i) {
        return expListTitle.get(i);

    }

    @Override
    public Object getChild(int i, int i1) {
        return expListDetail.get(
                expListTitle.get(i)
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
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             view = mInflater.inflate(R.layout.list_item_station, null);
        }
        TextView expListTextView = (TextView) view.findViewById(R.id.station_title);
        ImageView imageView = view.findViewById(R.id.img_view);
        Picasso.with(context)
                .load("https://image.ibb.co/butrtb/104_2.png")
                .into(imageView);
        expListTextView.setText(expListText.getTitle());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
