package com.airatikuzzz.radio.stations;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.airatikuzzz.radio.ItemMovedEvent;
import com.google.gson.annotations.SerializedName;

/**
 * Created by maira on 24.01.2018.
 */

public class RadioStation  {
    @SerializedName("url")
    String url;
    @SerializedName("name")
    String title;
    @SerializedName("iconUrl")
    String iconUrl;
    @SerializedName("info")
    String info;

    public RadioStation() {
    }

    public RadioStation(String url, String title, String info, String iconUrl, Bitmap icon) {
        this.url = url;
        this.title = title;
        this.iconUrl = iconUrl;
        this.info = info;
        this.icon = icon;
    }

    @SerializedName("icon")
    Bitmap icon;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

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

}
