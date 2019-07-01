package com.troology.mygate.fcm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.UserMeetings;
import com.troology.mygate.dashboard.ui.Dashboard;
import com.troology.mygate.splash.ui.SplashActivity;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.UtilsMethods;

import org.json.JSONObject;

import java.util.Objects;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    BroadcastReceiver mReceiver = new NotificationReceiver();
    String tag;

    @Override
    public void onCreate() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        tag = remoteMessage.getData().get("body");
        UserMeetings model = new Gson().fromJson(remoteMessage.getData().get("body"), UserMeetings.class);
        Log.e(TAG, "RemoteMessage: " + remoteMessage.getData().get("requestId")+" -- "+ remoteMessage.getData().get("title")
                +" -- "+ remoteMessage.getData().get("body")+" -- "+ remoteMessage.getData().get("body"));

        if (remoteMessage.getData() != null) {
            if (model.getRequest_id()!=null){
                Intent broadcastIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
                broadcastIntent.putExtra("request", tag);
                sendBroadcast(broadcastIntent);
            }
            methodNotify(remoteMessage.getData().get("title"), model.getName()+" is on the gate for, "+model.getRemarks());

        } else {
            Log.e(TAG, "FCM Notification failed");
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e(TAG, "onNewToken: >>>> " + s);
        if (s != null) {
            UtilsMethods.INSTANCE.save(getApplicationContext(), ApplicationConstant.INSTANCE.fireBaseToken, s);
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    private void methodNotify(String title, String messageBody) {
        String channelId = getString(R.string.app_name);
        PendingIntent pendingIntent;
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(genNotifyId(), notificationBuilder.build());
    }

    public int genNotifyId() {
        Random r = new Random(System.currentTimeMillis());
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

}
