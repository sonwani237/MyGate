package com.troology.mygate.splash.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.login_reg.ui.LoginScreen;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.UtilsMethods;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Splash" ;
    private static int SPLASH_TIME_OUT = 3000;
    RelativeLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        parent = findViewById(R.id.parent);

        Log.e(TAG, "onCreate: "+UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.fireBaseToken, String.class) );

        if (UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class)!=null &&
                UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getMobile().length() > 0){

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("mobile", UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getMobile());

            UtilsMethods.INSTANCE.ApartmentsDetail(SplashActivity.this, jsonObject, parent, null);

//                    startActivity(new Intent(SplashActivity.this, Dashboard.class));
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginScreen.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}
