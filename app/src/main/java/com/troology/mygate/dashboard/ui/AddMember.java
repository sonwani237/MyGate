package com.troology.mygate.dashboard.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import java.io.File;

public class AddMember extends AppCompatActivity implements View.OnClickListener {

    ImageView img;
    EditText ed_name, ed_number;
    Button addMember;
    Loader loader;
    LinearLayout parent;
    ApartmentDetails details;
    int requestCode = 100;
    private File output = null;
    private int CONTENT_REQUEST = 1337;

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
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted so ask for permissions
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, requestCode);
                }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                output = new File(dir, System.currentTimeMillis()+".jpeg");
                Uri photoURI = FileProvider.getUriForFile(AddMember.this, getApplicationContext().getPackageName() + ".fileprovider", output);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CONTENT_REQUEST);
                break;
            case R.id.addMember:
                if (isValid()) {
                    if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {
                        addMember.setEnabled(false);
                        loader.show();
                        loader.setCancelable(false);
                        loader.setCanceledOnTouchOutside(false);

                        String token = UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken();
                        UtilsMethods.INSTANCE.addMember(AddMember.this, output.getPath(),
                                details.getFlat_id(), details.getApartment_id(), token, ed_name.getText().toString().trim(),
                                ed_number.getText().toString().trim(), parent, loader);
                    } else {
                        UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), parent);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addMember.setEnabled(true);
                        }
                    }, 1500);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTENT_REQUEST) {

            if (resultCode == RESULT_OK) {
                Uri imageUri = Uri.fromFile(output);
                Log.d("pathhhhhh", "onActivityResult: " + output);
                Glide.with(this).load(imageUri)
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .into(img);
            }
        }
    }
}
