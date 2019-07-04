package com.troology.mygate.dashboard.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ActivityActivityMessage;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.GlobalBus;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class AddMember extends AppCompatActivity implements View.OnClickListener {

    ImageView img;
    EditText ed_name, ed_number;
    Button addMember;
    Loader loader;
    LinearLayout parent;
    ApartmentDetails details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_member);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        details = UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);
        img = findViewById(R.id.img);
        ed_name = findViewById(R.id.ed_name);
        ed_number = findViewById(R.id.ed_number);
        addMember = findViewById(R.id.addMember);
        parent = findViewById(R.id.parent);

        img.setOnClickListener(this);
        addMember.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img:
                break;
            case R.id.addMember:
                if (isValid()){
                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {
                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        JsonObject object = new JsonObject();
                        object.addProperty("flat_id", details.getFlat_id());
                        object.addProperty("token", UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
                        object.addProperty("name", ed_name.getText().toString().trim());
                        object.addProperty("mobile", ed_number.getText().toString().trim());

                        UtilsMethods.INSTANCE.addMember(getApplicationContext(), object, parent, loader);
                    } else {
                        UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
                    }
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            GlobalBus.getBus().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @SuppressLint("RestrictedApi")
    @Subscribe
    public void onActivityActivityMessage(ActivityActivityMessage activityFragmentMessage) {
        if (activityFragmentMessage.getMessage().equalsIgnoreCase("MemberAdd")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onBackPressed();
                }
            }, 1000);
        }
    }

    public boolean isValid() {

        if (ed_name.getText().toString().equalsIgnoreCase("")) {
            UtilsMethods.INSTANCE.snackBar("Name cannot be blank!", parent);
            return false;
        }

        if (ed_number.getText().toString().length() != 10) {
            UtilsMethods.INSTANCE.snackBar("Please enter valid Mobile Number!", parent);
            return false;
        }

        return true;
    }

}
