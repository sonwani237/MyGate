package com.troology.mygate.dashboard.ui;

import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.UserMeetings;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    String request = "";
    UserMeetings model;
    TextView name, remark;
    ImageView cancel, approve;
    Loader loader;
    RelativeLayout parent;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int flags = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().addFlags(flags);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        request = getIntent().getStringExtra("request");
        Log.e("onCreate "," >>> "+request);
        model = new Gson().fromJson(request, UserMeetings.class);
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

        name = findViewById(R.id.name);
        remark = findViewById(R.id.remark);
        parent = findViewById(R.id.parent);

        cancel = findViewById(R.id.cancel);
        approve = findViewById(R.id.approve);

        name.setText(model.getName());
        remark.setText(model.getRemarks());

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();

        cancel.setOnClickListener(this);
        approve.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.approve:
                sendRequest(1);
                break;
            case R.id.cancel:
                sendRequest(3);
                break;
        }
    }

    public void sendRequest(int i) {
        if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

            ringtone.stop();
            loader.show();
            loader.setCancelable(false);
            loader.setCanceledOnTouchOutside(false);

            JsonObject object = new JsonObject();
            object.addProperty("status", model.getStatus());
            object.addProperty("request_id", model.getRequest_id());
            object.addProperty("apartment_id", model.getApartment_id());
            object.addProperty("flat_id", model.getFlat_id());
            object.addProperty("token", UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
            if (i == 1) {
                object.addProperty("action", "1");
            } else {
                object.addProperty("action", "3");
            }

            UtilsMethods.INSTANCE.RequestAction(this, object, parent, loader);
        } else {
            UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        ringtone.stop();
//        finish();
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ringtone.stop();
//        finish();
//    }

}
