package com.troology.mygate.dashboard.ui;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.dashboard.model.UserMeetings;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.login_reg.ui.LoginScreen;
import com.troology.mygate.splash.model.NotificationModel;
import com.troology.mygate.splash.ui.SplashActivity;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    String request = "";
    NotificationModel model;
    TextView name, remark;
    ImageView cancel, approve;
    Loader loader;
    RelativeLayout parent;
    Ringtone ringtone;
    ImageView user_img;
    int SPLASH_TIME_OUT = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int flags = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        getWindow().addFlags(flags);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        request = getIntent().getStringExtra("request");
        Log.e("onCreate "," >>> "+request);
        model = new Gson().fromJson(request, NotificationModel.class);
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

        name = findViewById(R.id.name);
        remark = findViewById(R.id.remark);
        parent = findViewById(R.id.parent);
        user_img = findViewById(R.id.user_img);

        cancel = findViewById(R.id.cancel);
        approve = findViewById(R.id.approve);

        name.setText(model.getName());
        remark.setText(model.getRemarks());


        Glide.with(this).load(ApplicationConstant.INSTANCE.baseUrl+"/"+model.getImage())
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .error(getResources().getDrawable(R.drawable.man))
                .into(user_img);

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();

        cancel.setOnClickListener(this);
        approve.setOnClickListener(this);

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, SPLASH_TIME_OUT);*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.approve:
                sendRequest(1);
                break;
            case R.id.cancel:
                sendRequest(2);
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
            object.addProperty("token", UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
            object.addProperty("ref_id", model.getRecordId());
            object.addProperty("activity", "1");
            object.addProperty("request_by", "1");
            object.addProperty("approval", i);

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
