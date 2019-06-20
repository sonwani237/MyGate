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
        String state = k2.getStringExtra("requestId");
        Toast.makeText(k1, ">>>"+state, Toast.LENGTH_LONG).show();
        k1.startActivity(new Intent(k1, NotificationActivity.class));
    }

}