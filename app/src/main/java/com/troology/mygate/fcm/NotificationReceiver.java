package com.troology.mygate.fcm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.troology.mygate.dashboard.ui.NotificationActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context k1, Intent k2) {
        String request = k2.getStringExtra("request");
        k1.startActivity(new Intent(k1, NotificationActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("request", request));
    }

}