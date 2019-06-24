package com.troology.mygate.dashboard.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.splash.ui.SplashActivity;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.UtilsMethods;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back, iv_logout;
    ApartmentDetails details;
    TextView tv_name, tv_email, tv_number, tv_flatdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        details = UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);

        iv_back = findViewById(R.id.iv_backuserprofile);
        iv_logout = findViewById(R.id.iv_logout);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_number = findViewById(R.id.tv_number);
        tv_flatdetails = findViewById(R.id.tv_flatdetails);

        iv_back.setOnClickListener(this);
        iv_logout.setOnClickListener(this);

        tv_name.setText(details.getUsername());
        tv_email.setText(UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getEmail());
        tv_number.setText(UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getMobile());
        tv_flatdetails.setText(details.getFlat_no()+", "+details.getApartment_name()+", "+details.getCity_name()+", "+details.getState_name()+", "+details.getCountry_name());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backuserprofile:
                onBackPressed();
                break;
            case R.id.iv_logout:
                UtilsMethods.INSTANCE.save(this, ApplicationConstant.INSTANCE.userToken, "");
                Intent intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                break;
        }
    }

}
