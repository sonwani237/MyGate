package com.troology.mygate.dashboard.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.troology.mygate.R;

public class QrCodeActivity extends AppCompatActivity {
    ImageView qr_code_img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);
        initView();

    }
    private void initView(){
        Intent intent = getIntent();
        Bitmap bitmap =intent.getParcelableExtra("BitmapImage");
        qr_code_img= findViewById(R.id.qr_code_img);
        qr_code_img.setImageBitmap(bitmap);
    }
}
