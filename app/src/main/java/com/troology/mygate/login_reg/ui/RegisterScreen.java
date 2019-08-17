package com.troology.mygate.login_reg.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {

    EditText name, email;
    Button submit;
    Loader loader;
    String mobile= "";
    RelativeLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mobile = getIntent().getStringExtra("mobile");
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        parent = findViewById(R.id.parent);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        submit = findViewById(R.id.submit);

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
                object.addProperty("mobile", mobile);
                object.addProperty("name", name.getText().toString());
                object.addProperty("email", email.getText().toString());

                UtilsMethods.INSTANCE.register(RegisterScreen.this, object, parent, loader);
            } else {
                UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
            }
        }
    }

}
