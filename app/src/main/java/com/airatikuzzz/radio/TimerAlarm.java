package com.airatikuzzz.radio;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.airatikuzzz.radio.player.RadioManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by maira on 20.02.2018.
 */

public class TimerAlarm extends BroadcastReceiver {
    public static final String TIMER_5_MIN = "5 мин";
    public static final String TIMER_15_MIN = "15 мин";
    public static final String TIMER_30_MIN = "30 мин";
    public static final String TIMER_1_HOUR = "1 час";
    public static final String TIMER_2_HOUR = "2 часа";
    public static final String TIMER_3_SEC = "3 сек";

    public static String[]timers = {TIMER_5_MIN, TIMER_15_MIN, TIMER_30_MIN, TIMER_1_HOUR, TIMER_2_HOUR};


    public static String[] getTimers() {
        return timers;
    }

    public TimerAlarm() {
    }

    public TimerAlarm(Context context, String timer){
        int timeoutInSeconds = 0;
        switch (timer){
            case TIMER_3_SEC: timeoutInSeconds = 3000;break;
            case TIMER_5_MIN: timeoutInSeconds = 5*60*1000;break;
            case TIMER_15_MIN: timeoutInSeconds = 15*60*1000;break;
            case TIMER_30_MIN: timeoutInSeconds = 35*60*1000;break;
            case TIMER_1_HOUR: timeoutInSeconds = 60*60*1000;break;
            case TIMER_2_HOUR: timeoutInSeconds = 120*60*1000;break;
        }
        AlarmManager alarmMgr =
                (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimerAlarm.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.MILLISECOND, timeoutInSeconds);
        Log.d("kek3", "on timerAlarm() "+ timeoutInSeconds);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
                pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("kek3", "Onreceive");
        RadioManager manager = RadioManager.with(context);
        manager.stopPlayer();
    }
}
