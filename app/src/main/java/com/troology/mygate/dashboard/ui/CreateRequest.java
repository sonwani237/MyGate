package com.troology.mygate.dashboard.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import java.util.Objects;

public class CreateRequest extends AppCompatActivity implements View.OnClickListener {

    Loader loader;
    ApartmentDetails details;
    EditText ed_name, number, purpose;
    Button submit;
    RelativeLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.create_req);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);

        parent = findViewById(R.id.parent);
        ed_name = findViewById(R.id.ed_name);
        number = findViewById(R.id.number);
        purpose = findViewById(R.id.purpose);
        submit = findViewById(R.id.submit);

        details = UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == submit){
            if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {

                loader.show();
                loader.setCancelable(false);
                loader.setCanceledOnTouchOutside(false);

                JsonObject object = new JsonObject();
                object.addProperty("flat_id", details.getFlat_id());
                object.addProperty("apartment_id", details.getApartment_id());
                object.addProperty("token", UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken());
                object.addProperty("request_by", "1");
                object.addProperty("name", ed_name.getText().toString());
                object.addProperty("mobile", number.getText().toString());
                object.addProperty("meeting_time", "2019-06-20 06:31:50");
                object.addProperty("remarks", purpose.getText().toString());
                object.addProperty("status", "1");

                UtilsMethods.INSTANCE.AddRequest(getApplicationContext(), object, parent, loader);
            } else {
                UtilsMethods.INSTANCE.snackBar("", parent);
            }
        }
    }
}
