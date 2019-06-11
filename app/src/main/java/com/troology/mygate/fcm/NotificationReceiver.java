package com.troology.mygate.fcm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.troology.mygate.dashboard.ui.NotificationActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context k1, Intent k2) {
        Toast.makeText(k1, "Notification received!", Toast.LENGTH_LONG).show();
        k1.startActivity(new Intent(k1, NotificationActivity.class));
    }

}