package com.troology.mygate.dashboard.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.troology.mygate.R;
import com.troology.mygate.login_reg.model.ApartmentDetails;
import com.troology.mygate.login_reg.model.UserDetails;
import com.troology.mygate.splash.ui.SplashActivity;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.Loader;
import com.troology.mygate.utils.UtilsMethods;

import java.io.File;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back, iv_logout, qr_code_img, propicc;
    ApartmentDetails details;
    TextView tv_name, tv_email, tv_number, tv_flatdetails;
    Bitmap bitmap;
    int requestCode = 100;
    private File output = null;
    private int CONTENT_REQUEST = 1337;
    RelativeLayout rellay_profile;
    String imgPath = "";
    ProgressBar pbProfile;
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        details = UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);
        loader = new Loader(this, android.R.style.Theme_Translucent_NoTitleBar);
        iv_back = findViewById(R.id.iv_backuserprofile);
        iv_logout = findViewById(R.id.iv_logout);
        qr_code_img = findViewById(R.id.qr_code_img);
        propicc = findViewById(R.id.propic);
        rellay_profile = findViewById(R.id.rellay_profile);
        pbProfile = findViewById(R.id.pbProfile);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_number = findViewById(R.id.tv_number);
        tv_flatdetails = findViewById(R.id.tv_flatdetails);

        iv_back.setOnClickListener(this);
        iv_logout.setOnClickListener(this);
        qr_code_img.setOnClickListener(this);
        propicc.setOnClickListener(this);

//        pbProfile.setVisibility(View.VISIBLE);

        imgPath = UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.profilePic, String.class);

        Glide.with(this).load(ApplicationConstant.INSTANCE.baseUrl+"/"+imgPath)
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .error(getResources().getDrawable(R.drawable.man))
                .into(propicc);

        tv_name.setText(details.getUsername());
        tv_email.setText(UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getEmail());
        tv_number.setText(UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getMobile());
        tv_flatdetails.setText(details.getFlat_no() + ", " + details.getApartment_name() + ", " + details.getCity_name() + ", " + details.getState_name() + ", " + details.getCountry_name());
        String text = UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.userPassPerf, String.class); // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr_code_img.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backuserprofile:
                onBackPressed();
                break;

            case R.id.iv_logout:
                UtilsMethods.INSTANCE.save(this, ApplicationConstant.INSTANCE.userToken, "");
                UtilsMethods.INSTANCE.save(this, ApplicationConstant.INSTANCE.appPref, "");
                UtilsMethods.INSTANCE.save(this, ApplicationConstant.INSTANCE.profilePic, "");
                UtilsMethods.INSTANCE.save(this, ApplicationConstant.INSTANCE.flatPerf, "");
                UtilsMethods.INSTANCE.save(this, ApplicationConstant.INSTANCE.userPassPerf, "");
                Intent intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                break;

            case R.id.qr_code_img:
                Intent i = new Intent(this, QrCodeActivity.class);
                //i.putExtra("BitmapImage", bitmap);
                i.putExtra("name", details.getUsername());
                i.putExtra("passcode", UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.userPassPerf, String.class)); // Whatever you need to encode in the QR code);
                i.putExtra("image", UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.profilePic, String.class));
                startActivity(i);
                break;

            case R.id.propic:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted so ask for permissions
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, requestCode);
                }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                output = new File(dir, System.currentTimeMillis()+".jpeg");
                Uri photoURI = FileProvider.getUriForFile(UserProfile.this,
                        getApplicationContext().getPackageName() + ".fileprovider", output);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CONTENT_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTENT_REQUEST) {

            if (resultCode == RESULT_OK) {
                Uri imageUri;
                imageUri = Uri.fromFile(output);
                Log.d("pathhhhhh", "onActivityResult: " + output);
                Glide.with(this).load(imageUri)
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .into(propicc);
                String token = UtilsMethods.INSTANCE.get(getApplicationContext(), ApplicationConstant.INSTANCE.loginPerf, UserDetails.class).getToken();

                if (UtilsMethods.INSTANCE.isNetworkAvailable(getApplicationContext())) {
                    loader.show();
                    loader.setCancelable(false);
                    loader.setCanceledOnTouchOutside(false);

                    UtilsMethods.INSTANCE.uploadImage(UserProfile.this, output.getPath(),
                            details.getUid(),token, rellay_profile, loader);
                } else {
                    UtilsMethods.INSTANCE.snackBar(getResources().getString(R.string.network_error), rellay_profile);
                }
            }
        }
    }

}
