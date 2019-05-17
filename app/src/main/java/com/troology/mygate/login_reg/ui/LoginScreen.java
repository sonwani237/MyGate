package com.troology.mygate.login_reg.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.troology.mygate.R;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

public class LoginScreen extends AppCompatActivity {

    EditText number;
    RelativeLayout parent;
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        number = findViewById(R.id.number);
        parent = findViewById(R.id.parent);
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length()==10){
                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())){
                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        JsonObject object = new JsonObject();
                        object.addProperty("mobile", s.toString());

                        UtilsMethods.INSTANCE.sendOTP(getApplicationContext(), object, parent, loader);
                    }else {
                        UtilsMethods.INSTANCE.snackBar("", parent);
                    }

                }
            }
        });
    }
}
