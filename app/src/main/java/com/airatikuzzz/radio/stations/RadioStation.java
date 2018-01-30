package com.airatikuzzz.radio.stations;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maira on 24.01.2018.
 */

public class RadioStation  {
    @SerializedName("url")
    String url;
    @SerializedName("name")
    String title;
   // @SerializedName("icon")
    String iconUrl;
    Bitmap icon;

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RadioStation(String title) {
        this.title = title;
    }

}
