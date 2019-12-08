package com.application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {
    public static final String CHANNEL_ID="ryc_channel";

    private static RequestQueue mQueue;
    @Override
    public void onCreate(){
        super.onCreate();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"ryc",NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }
    public static RequestQueue getQueue(){
        return mQueue;
    }
}
