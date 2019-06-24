package com.troology.mygate.login_reg.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chaos.view.PinView;
import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

public class OTPVerification extends AppCompatActivity implements View.OnClickListener {

    PinView otp_code;
    Button verify;
    Loader loader;
    String mobile= "";
    RelativeLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        mobile = getIntent().getStringExtra("mobile");
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        parent = findViewById(R.id.parent);
        otp_code = findViewById(R.id.otp_code);
        verify = findViewById(R.id.verify);

        verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == verify) {
            Log.e("onClick: ", ">>> " + otp_code.getText().toString());
            if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {
                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                String fcm = UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.fireBaseToken, String.class);

                JsonObject object = new JsonObject();
                object.addProperty("mobile", mobile);
                object.addProperty("otp", otp_code.getText().toString());
                object.addProperty("fcm_token", fcm);

                UtilsMethods.INSTANCE.verifyOTP(getApplicationContext(), object, parent, loader);
            } else {
                UtilsMethods.INSTANCE.snackBar("", parent);
            }
        }
    }
}
