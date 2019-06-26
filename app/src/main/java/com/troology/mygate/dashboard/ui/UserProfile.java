package com.troology.mygate.dashboard.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.troology.mygate.utils.UtilsMethods;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back, iv_logout, qr_code_img;
    ApartmentDetails details;
    TextView tv_name, tv_email, tv_number, tv_flatdetails;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        details = UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.flatPerf, ApartmentDetails.class);

        iv_back = findViewById(R.id.iv_backuserprofile);
        iv_logout = findViewById(R.id.iv_logout);
        qr_code_img = findViewById(R.id.qr_code_img);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_number = findViewById(R.id.tv_number);
        tv_flatdetails = findViewById(R.id.tv_flatdetails);

        iv_back.setOnClickListener(this);
        iv_logout.setOnClickListener(this);
        qr_code_img.setOnClickListener(this);

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
                Intent intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                break;
            case R.id.qr_code_img:
                Intent i = new Intent(this, QrCodeActivity.class);
                //i.putExtra("BitmapImage", bitmap);
                i.putExtra("name",details.getUsername());
                i.putExtra("passcode",UtilsMethods.INSTANCE.get(this, ApplicationConstant.INSTANCE.userPassPerf, String.class)); // Whatever you need to encode in the QR code);
                startActivity(i);
                break;
        }
    }
}
