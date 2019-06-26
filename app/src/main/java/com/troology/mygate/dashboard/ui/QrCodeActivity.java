package com.troology.mygate.dashboard.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.troology.mygate.R;
import com.troology.mygate.utils.ApplicationConstant;
import com.troology.mygate.utils.UtilsMethods;

public class QrCodeActivity extends AppCompatActivity {
    ImageView qr_code_img,back;
    String name;
    String passcode;
    Bitmap bitmap;
    TextView tvName,tvCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code);
        tvName = findViewById(R.id.qrName);
        tvCode = findViewById(R.id.qrpascode);
        back = findViewById(R.id.backqr);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initView();

    }
    private void initView(){
        Intent intent = getIntent();
        //Bitmap bitmap =intent.getParcelableExtra("BitmapImage");
        name = (String) intent.getExtras().get("name");
        passcode = (String) intent.getExtras().get("passcode");
        qr_code_img= findViewById(R.id.qr_code_img);


        tvName.setText("Name : "+name);
        tvCode.setText("Passcode : "+passcode);


        Toast.makeText(this, ""+passcode, Toast.LENGTH_SHORT).show();


        Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();




        String text = passcode; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr_code_img.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }






       // qr_code_img.setImageBitmap(bitmap);
    }
}
